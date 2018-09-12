package com.wolf.lib.okhttpcool.cache;

import android.text.TextUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

/**
 * @author Roye
 * @date 2018/9/10
 */
public class CacheStrategyUtil {

    public static final String STRATEGY_KEY = "adrcache";

    public static void checkSame(okhttp3.Response resopnse1, okhttp3.Response resopnse2, Consumer<String> consumer) {
        Observable.just(resopnse1, resopnse2).flatMap(new Function<okhttp3.Response, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(final okhttp3.Response resopnse) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        try {
                            String md5 = ByteString.of(resopnse.body().bytes()).md5().hex();
                            e.onNext(String.valueOf(md5));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        e.onNext("");
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation());
            }
        }).reduce(new BiFunction<String, String, String>() {
                      @Override
                      public String apply(String o, String o2) {
                          return o.equals(o2) ? "y" : "n";
                      }
                  }
        ).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(consumer);
    }

    public static Request getCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "max-age=86400").build();
    }

    public static Request getNoCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "no-cache").build();
    }

    static WeakReference<HashMap<String, Integer>> weakReference;

    public static Response doForCacheInterceptor(Interceptor.Chain chain, Request oRequest) throws IOException {
        String adrcache = oRequest.header(CacheStrategyUtil.STRATEGY_KEY);

        if (TextUtils.isEmpty(adrcache)) {
            return null;
        }

        Request request = oRequest;
        if (adrcache.equals(CacheStrategy.getcache.name())) {
            request = CacheStrategyUtil.getCacheRequest(oRequest);
        } else if (adrcache.equals(CacheStrategy.refresh.name())) {
            request = CacheStrategyUtil.getNoCacheRequest(oRequest);
        } else if (adrcache.equals(CacheStrategy.getandrefresh.name())) {
            if (weakReference == null || weakReference.get() == null) {
                weakReference = new WeakReference<>(new HashMap<String, Integer>());
            }

            HashMap<String, Integer> map = weakReference.get();
            if (map == null) {
                map = new HashMap<>();
                weakReference = new WeakReference<>(map);
            }
            String tmpKey = Cache.key(oRequest.url());
            if (!map.containsKey(tmpKey)) {//第一次
                request = CacheStrategyUtil.getCacheRequest(oRequest);
                map.put(tmpKey, 1);
            } else {
                request = CacheStrategyUtil.getNoCacheRequest(oRequest);
                map.remove(tmpKey);
            }
        }
        return chain.proceed(request);
    }

    public static Response doForNetworkInterceptor(Interceptor.Chain chain, Request oRequest) throws IOException {
        String adrcache = oRequest.header(CacheStrategyUtil.STRATEGY_KEY);

        if (TextUtils.isEmpty(adrcache)) {
            return null;
        }

        Request request = oRequest.newBuilder().removeHeader(CacheStrategyUtil.STRATEGY_KEY).build();
        Response originalResponse = chain.proceed(request);
        return originalResponse.newBuilder()
                .removeHeader("Pragma")//清除响应体对Cache有影响的信息
                .removeHeader("Cache-Control")//清除响应体对Cache有影响的信息
                .header("Cache-Control", "max-age=86400") //秒
                .build();
    }

}
