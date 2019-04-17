package com.supylc.network.build;

import android.text.TextUtils;

import com.supylc.network.internal.CommonParamsAdapter;
import com.supylc.network.support.ApiBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;

public class NetworkBuilder {

    public <T> T build() {
        return (T) build(apiClass);
    }

    private <T> T build(Class<T> apiClass) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException(String.format("baseUrl is not set?", apiClass.getName()));
        }
        apiBuilder.forType(apiClass);

        apiBuilder.baseUrl(baseUrl);
        apiBuilder.isSubscribeOnUiThread(subscribeOnBackground);
        apiBuilder.setGlobalRequestAdapter(commonParamsAdapter);
        if (connectTimeoutSecond > 0) {
            apiBuilder.getOkHttpBuilder().connectTimeout(connectTimeoutSecond, TimeUnit.SECONDS);
        }
        if (readTimeoutSecond > 0) {
            apiBuilder.getOkHttpBuilder().connectTimeout(readTimeoutSecond, TimeUnit.SECONDS);
        }
        if (cache != null) {
            apiBuilder.getOkHttpBuilder().cache(cache);
        }
        if (cookieJar != null) {
            apiBuilder.getOkHttpBuilder().cookieJar(cookieJar);
        }

        if (apiClass == null) {
            return null;
        }

        T api = (T) apiBuilder.build();
        ApiUtil.setApi(apiClass.getName(), api);
        return api;
    }

    static NetworkBuilder with(ApiBuilder apiBuilder) {
        NetworkBuilder builder = new NetworkBuilder();
        builder.apiBuilder = apiBuilder;
        return builder;
    }

    private ApiBuilder apiBuilder;

    private String baseUrl;
    private CommonParamsAdapter commonParamsAdapter;
    private boolean subscribeOnBackground;
    private long connectTimeoutSecond;
    private long readTimeoutSecond;
    private CookieJar cookieJar;
    private Cache cache;
    private Class apiClass;

    NetworkBuilder apiClass(Class apiClass) {
        this.apiClass = apiClass;
        return this;
    }

    public NetworkBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public NetworkBuilder requestAdapter(CommonParamsAdapter commonParamsAdapter) {
        this.commonParamsAdapter = commonParamsAdapter;
        return this;
    }

    public NetworkBuilder subscribeOnBackground(boolean subscribeOnBackground) {
        this.subscribeOnBackground = subscribeOnBackground;
        return this;
    }

    public NetworkBuilder connectTimeout(long timeoutSecond) {
        this.connectTimeoutSecond = timeoutSecond;
        return this;
    }

    public NetworkBuilder readTimeout(long timeoutSecond) {
        this.readTimeoutSecond = timeoutSecond;
        return this;
    }

    public NetworkBuilder cookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    public NetworkBuilder cache(Cache cache) {
        this.cache = cache;
        return this;
    }

}