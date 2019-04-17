package com.wolf.lib.okhttpcool.demo;

import com.supylc.network.build.ApiGroup;
import com.supylc.network.support.cache.CacheStrategy;
import com.wolf.lib.okhttpcool.demo.bean.TaobaoTest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

@ApiGroup(group = "main")
public interface CoolAPi {

    @Headers(CacheStrategy.ONLY_CACHE)
    @GET("sug?code=utf-8")
    Observable<ZHttpResponse<List<List<String>>>> testSearchOnlyCache(@Query("q") String keyword);

    @Headers(CacheStrategy.NETWORK)
    @GET("sug?code=utf-8")
    Observable<ZHttpResponse<List<List<String>>>> testSearchNetwork(@Query("q") String keyword);

    @Headers(CacheStrategy.CACHE_1_HOUR)
    @GET("sug?code=utf-8")
    Observable<ZHttpResponse<List<List<String>>>> testSearchCacheAge(@Query("q") String keyword);

    @Headers(CacheStrategy.CACHE_AND_REFRESH)
    @GET("sug?code=utf-8")
    Observable<ZHttpResponse<List<List<String>>>> testSearchSceneCacheCall(@Query("q") String keyword);

    @Headers(CacheStrategy.CACHE)
    @GET("sug?code=utf-8")
    Observable<ZHttpResponse<List<List<String>>>> testSearchSceneCache(@Query("q") String keyword);

}
