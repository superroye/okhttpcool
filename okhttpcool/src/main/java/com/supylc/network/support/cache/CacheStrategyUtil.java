package com.supylc.network.support.cache;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;
import retrofit2.Call;

/**
 * @author Roye
 * @date 2018/9/10
 */
public class CacheStrategyUtil {

    //如果是KEY_CACHE_AND_REFRESH缓存策略，需要特殊处理，先调用缓存再请求网络
    public static Observable<Object> handleSpecialCache(Observable observable, Call call) {
        if ("GET".equals(call.request().method()) &&
                CacheStrategy.KEY_CACHE_AND_REFRESH.equals(call.request().header(CacheStrategy.HEADER_KEY))) {
            return observable.takeWhile(new Predicate<Object>() {
                Object last = null;

                @Override
                public boolean test(Object o) {
                    if (last == null) {
                        last = getData(o);
                        return true;
                    }

                    Object lastObj = last;
                    if (lastObj == null) {
                        return true;
                    }

                    if (o == null) {
                        return true;
                    }

                    Object currentObj = getData(o);

                    //如果两次请求一致，第二次不再通知
                    Gson gson = new Gson();
                    try {
                        String resultLast = gson.toJson(lastObj);
                        String resultCurrent = gson.toJson(currentObj);
                        String md5Last = ByteString.encodeUtf8(gson.toJson(resultLast)).md5().hex();
                        String md5Current = ByteString.encodeUtf8(gson.toJson(resultCurrent)).md5().hex();
                        return !md5Last.equals(md5Current);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                Object getData(Object o) {
                    Object data;
                    if (o instanceof retrofit2.Response) {
                        retrofit2.Response response = (retrofit2.Response) o;
                        data = response.raw().body();
                    } else {
                        data = o;
                    }
                    return data;
                }

            }).repeat(2);
        }
        return observable;
    }

    public static Request getCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "max-age=2592000").build();
    }

    public static Request getNoCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "no-cache").build();
    }

    public static Request getOnlyCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=2419200").build();
    }

    public static Request get1HourCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "max-age=3600").build();
    }

    public static Request getRefreshRequest(Request oRequest) {
        return getNoCacheRequest(oRequest);
    }

    static WeakHashMap<String, Integer> cacheAndRefreshFlag = new WeakHashMap<>();

    public static Response doForCacheInterceptor(Interceptor.Chain chain, Request oRequest) throws IOException {
        String adrcache = oRequest.header(CacheStrategy.HEADER_KEY);

        if (TextUtils.isEmpty(adrcache)) {
            return null;
        }

        Request request = oRequest;
        if (adrcache.equals(CacheStrategy.KEY_CACHE)) {

            request = getCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_ONLY_CACHE)) {

            request = getOnlyCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_CACHE_1_HOUR)) {

            request = get1HourCacheRequest(oRequest);

        }else if (adrcache.equals(CacheStrategy.KEY_NETWORK)) {

            request = getNoCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_CACHE_AND_REFRESH)) {
            if ("GET".equals(oRequest.method())) {
                String tmpKey = Cache.key(oRequest.url());
                if (!cacheAndRefreshFlag.containsKey(tmpKey)) {//第一次
                    request = getCacheRequest(oRequest);
                    cacheAndRefreshFlag.put(tmpKey, 1);
                } else {
                    request = getNoCacheRequest(oRequest);
                    cacheAndRefreshFlag.remove(tmpKey);
                }
            } else {
                request = getNoCacheRequest(oRequest);
            }
        }
        return chain.proceed(request);
    }

    public static Response doForNetworkInterceptor(Interceptor.Chain chain, Request oRequest) throws IOException {
        String adrcache = oRequest.header(CacheStrategy.HEADER_KEY);

        if (TextUtils.isEmpty(adrcache)) {
            return null;
        }

        CacheControl cacheControl = oRequest.cacheControl();

        Request request = oRequest.newBuilder().removeHeader(CacheStrategy.HEADER_KEY).build();
        Response originalResponse = chain.proceed(request);
        Response.Builder builder = originalResponse.newBuilder()
                .removeHeader("Pragma")//清除响应体对Cache有影响的信息
                .removeHeader("Cache-Control");//清除响应体对Cache有影响的信息

        if (cacheControl.maxAgeSeconds() > 0 || cacheControl.onlyIfCached()) {
            builder.header("Cache-Control", cacheControl.toString());
        } else {
            builder.header("Cache-Control", "max-age=2592000"); //秒
        }

        return builder.build();
    }

}
