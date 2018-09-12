package com.wolf.lib.okhttpcool.demo;

import com.wolf.lib.okhttpcool.cache.SceneCacheStrategy;
import com.wolf.lib.okhttpcool.demo.bean.City;
import com.wolf.lib.okhttpcool.demo.bean.TaobaoTest;
import com.wolf.lib.okhttpcool.response.HttpResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @Headers("Cache-Control: max-age=640000") 直接请求，有缓存且未过期返回缓存，否则读网络并写缓存
 * @Headers("Cache-Control:no-cache") 直接请求网络，不做缓存
 * @Headers("Cache-Control:no-store") 直接请求网络，不存临时缓存
 * @Headers("Cache-Control:public, only-if-cached, max-stale=2419200") 直接请求缓存
 *
 * @GET("select/search.php")
 * @GET("book/{id}")
 *
 * @POST
 * @FormUrlEncoded
 *
 */
public interface ZZCAPi {

    @Headers("Cache-Control:public, only-if-cached, max-stale=2419200")
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchOnlyCache(@Query("q") String keyword);

    @Headers("Cache-Control:no-cache")
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchNetwork(@Query("q") String keyword);

    @Headers("Cache-Control:max-age=640")
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchCacheAge(@Query("q") String keyword);

    @GET("sug?code=utf-8")
    public Call<TaobaoTest> testSearchSceneCacheCall(@Query("q") String keyword, @Header(SceneCacheStrategy.STRATEGY_KEY) String cacheStrategy);

    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchSceneCache(@Query("q") String keyword, @Header(SceneCacheStrategy.STRATEGY_KEY) String cacheStrategy);

}
