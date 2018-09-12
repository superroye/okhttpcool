# 概述

基于OKhttp+Rxjava2+Retrofit进行二次封装，优雅的okhttpclient+retrofit初始化设置，完整的拦截器配置，基于OKhttp缓存，封装无网络和其他所有场景的缓存策略支持

# 特性

## 1、okhttp作为一个经典流行的网络库。
  
## 2、基于OKhttp+Rxjava2+Retrofit。
  
  定义接口和注解风格的http接口调用方式；
  定义了通用的ZHttpClient，可以根据retrofit重用ZHttpClient, 创建多个httpApi；
  自动的json解析；
  log输出；
  全策略缓存支持；

## 3、通过拦截器修改request和response，支持了无网络的缓存，以及基于header的通用策略缓存（无视服务端缓存策略）

## 4、基于okhttp原生缓存，设计了各种缓存策略。

  比起另外集成独立的缓存，最高效的还是使用官方缓存。
  作者通过okhttp拦截器技术，通过修改request header和response header，达到穿透缓存和保存缓存的效果；
  同时增加了只读缓存，刷新缓存和读缓存并刷新三种策略。
  
  综上，本工程支持6种缓存策略，包含：只读缓存（无视过期时间），自定义缓存时间，读网络，固定缓存1天，读网络并刷新缓存，读缓存同时请求网络并写缓存。

# 使用步骤

## 1、权限

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    
## 2、库依赖
    
    包含OKhttp+Rxjava2+Retrofit相关库，具体看gradle配置
    
## 3、定义HttpApi

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
        public Call<TaobaoTest> testSearchSceneCacheCall(@Query("q") String keyword, @Header(SceneCacheStrategy.STRATEGY_KEY) String                 cacheStrategy);

        @GET("sug?code=utf-8")
        public Observable<TaobaoTest> testSearchSceneCache(@Query("q") String keyword, @Header(SceneCacheStrategy.STRATEGY_KEY) String               cacheStrategy);
    }
    
## 4、初始化retrofit和HttpApi

    public class ZZService {

        private static ZZCAPi zzcaPi;

        public static ZZCAPi api() {
            if (zzcaPi == null) {
                synchronized (ZZService.class){
                    if(zzcaPi == null){
                        init();
                    }
                }
            }
            return zzcaPi;
        }

        private static void init() {
            ApiBuilder<ZZCAPi> apiBuilder = new ApiBuilder<ZZCAPi>() {
            };
            apiBuilder.setGlobalRequestAdapter(new GlobalRequestAdapter() {
                @Override
                public void addHeader(Request.Builder builder) {
                    //builder.addHeader("head1","value");
                }

                @Override
                public void addQueryParams(HttpUrl.Builder builder) {
                    //builder.addEncodedQueryParameter("test","testName");
                }

                @Override
                public void addPostParams(FormBody.Builder builder) {
                    //builder.add("testPost", "testPostValue");
                }
            });
            apiBuilder.baseUrl("https://suggest.taobao.com/");
            zzcaPi = apiBuilder.build();
        }
    }

## 5、调用接口

  以下是6种缓存策略的调用方式。
  
  
    //只读缓存（无视过期时间）
    public void cacheOnly(View view) {
        ZZService.api().testSearchOnlyCache("零食").subscribe(new ResponseObserver<TaobaoTest>() {

            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheOnly ====== " + result.toString());
            }
        });
    }

    //自定义缓存时间
    public void cacheAge(View view) {
        ZZService.api().testSearchCacheAge("零食").subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheAge ====== " + result.toString());
            }
        });
    }

    //不缓存，读网络
    public void noCache(View view) {
        ZZService.api().testSearchNetwork("零食").subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "noCache ====== " + result.toString());
            }
        });
    }

    //固定缓存1天
    public void cacheLong(View view) {
        ZZService.api().testSearchSceneCache("零食", SceneCacheStrategy.Strategy.getcache.name()).subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheLong ====== " + result.toString());
            }
        });
    }

    //读网络，并存缓存
    public void refresh(View view) {
        ZZService.api().testSearchSceneCache("零食", SceneCacheStrategy.Strategy.refresh.name()).subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "refresh ====== " + result.toString());
            }
        });
    }

    //读缓存，同时请求网络，并写缓存
    public void cacheAndRefresh(View view) {
        PriorityCacheResponseCallback cb = new PriorityCacheResponseCallback<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheAndRefresh ====== " + result.toString());
            }
        };
        ZZService.api()
                .testSearchSceneCacheCall("零食", SceneCacheStrategy.Strategy.getandrefresh.name())
                .enqueue(cb);
    }
    
