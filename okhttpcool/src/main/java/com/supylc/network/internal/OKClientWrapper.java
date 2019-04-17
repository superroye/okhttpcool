package com.supylc.network.internal;

import android.annotation.SuppressLint;

import com.supylc.network.Utils;
import com.supylc.network.internal.interceptor.CacheStrategyInterceptor;
import com.supylc.network.internal.interceptor.NetworkInterceptor;
import com.supylc.network.internal.interceptor.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Roye
 * @date 2018/8/6
 */
public class OKClientWrapper {

    private OkHttpClient client = null;

    private CommonParamsAdapter commonParamsAdapter;

    private OkHttpClient.Builder builder;

    public OKClientWrapper() {
        init();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setCommonParamsAdapter(CommonParamsAdapter commonParamsAdapter) {
        this.commonParamsAdapter = commonParamsAdapter;
    }

    private void init() {
        int totalCacheSize = 64 * 1024 * 1024; // 64M
        Cache cache = new Cache(new File(Utils.getApp().getCacheDir(), "okhttp"), totalCacheSize);

        this.builder = new OkHttpClient.Builder()
                .cache(cache)
                //.sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        builder.addNetworkInterceptor(new NetworkInterceptor());

        /**
         * okhttp会自动添加request header，这里（addNetworkInterceptor）能打印完整的header, 调试阶段按需添加吧
         HttpLoggingInterceptor allHeadersLog = new HttpLoggingInterceptor();//这里为了打印完整的header
         allHeadersLog.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE);
         builder.addNetworkInterceptor(allHeadersLog);
         */

        List<Interceptor> interceptors = new ArrayList();

        interceptors.add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();

                if (commonParamsAdapter != null) {
                    commonParamsAdapter.addHeader(requestBuilder);

                    HttpUrl.Builder httpUrlBuilder = originalRequest.url().newBuilder();
                    commonParamsAdapter.addQueryParams(originalRequest, httpUrlBuilder);
                    requestBuilder.url(httpUrlBuilder.build());

                    if ("POST".equals(originalRequest.method())) {
                        commonParamsAdapter.addPostParams(originalRequest, requestBuilder);
                    }
                }

                return chain.proceed(requestBuilder.build());
            }
        });

        interceptors.add(new CacheStrategyInterceptor());

        interceptors.add(new HttpLoggingInterceptor());

        builder.interceptors().addAll(interceptors);
    }

    public OkHttpClient.Builder builder() {
        return builder;
    }

    public void build() {
        client = builder.build();
    }

    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}

