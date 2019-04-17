# 概述

基于Retrofit进行二次封装，优雅的初始化设置。基于Okhttp拦截器和基础缓存，封装了无网络和其他所有场景的缓存策略支持，以及频繁调用请求的丢弃策略。

# 特性
  
## 1、基于Retrofit的二次封装。
  
  简洁的链式初始化方式；
  定义接口和注解风格的http接口调用方式；
  自动的Api管理；
  自动的json解析；
  log输出；
  全策略缓存支持；
  请求舍弃策略支持；
  请求loading支持；

## 2、基于okhttp原生缓存，设计了各种缓存策略。

比起外部集成独立的缓存，最高效的还是使用官方缓存。
通过okhttp拦截器技术，通过修改request header和response header，达到穿透缓存和保存缓存的效果；

本工程支持6种缓存策略，包含：只读缓存（无视过期时间），自定义缓存时间，读网络，固定缓存1天，读网络并刷新缓存，读缓存同时请求网络并写缓存。

注意：读缓存同时请求网络，能实现快速显示缓存，并同时请求网络，网络返回并检查缓存，数据不一致，则通知UI更新。达到页面秒开和检查最新数据的效果。

# 使用步骤

## 1、权限

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    
## 2、库依赖
    
    包含OKhttp+Rxjava2+Retrofit相关库，具体看gradle配置
    
## 3、定义HttpApi

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
    
## 4、初始化retrofit和HttpApi

        ApiManager.setDebug(true);
        ApiManager.with(this)
                .groupBuilder("main")
                .baseUrl("https://suggest.taobao.com/")
                .cache(new Cache(new File(getCacheDir()+"/okhttpcache"), 64*1024*1024))
                .connectTimeout(10)
                .readTimeout(10)
                .requestAdapter(new CommonParamsAdapter() {
                    @Override
                    public void addHeader(Request.Builder builder) {

                    }

                    @Override
                    public void addQueryParams(Request originalRequest, HttpUrl.Builder httpUrlBuilder) {

                    }

                    @Override
                    public void addPostParams(Request originalRequest, Request.Builder requestBuilder) {

                    }
                })
                .build();

## 5、调用接口

  ApiManager.api(CoolAPi.class).testSearchSceneCache("零食").subscribe(new ZResponseDataObserver<List<List<String>>>(this) {
            @Override
            public void onResponse(List<List<String>> result) {
                Log.d("okhttp", "cacheLong ====== " + result.toString());
            }
        });
  
   //如果是多次调用请求，类似搜索模糊提示的场景，可以通过下面方式，只取最后的请求结果。类似的，还有keepFirst策略。
   ApiManager.api(CoolAPi.class).testSearchOnlyCache("零食").compose(RxRequestUtils.keepLast());
    
