package com.supylc.network.support;

import com.supylc.network.internal.CommonParamsAdapter;
import com.supylc.network.internal.OKClientWrapper;
import com.supylc.network.support.cache.CacheStrategyUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Roye
 * @date 2018/10/19
 */
public class ApiBuilder<T> {

    protected OKClientWrapper httpClient;

    private Class<T> cls;
    private String baseUrl;
    private boolean mIsSubscribeOnUiThread = true;

    public ApiBuilder() {
        httpClient = new OKClientWrapper();
    }

    public OkHttpClient getClient() {
        return httpClient.getClient();
    }

    public ApiBuilder forType(Class<T> cls) {
        this.cls = cls;
        return this;
    }

    public ApiBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public ApiBuilder isSubscribeOnUiThread(boolean isSubscribeOnUiThread) {
        this.mIsSubscribeOnUiThread = isSubscribeOnUiThread;
        return this;
    }

    public ApiBuilder setGlobalRequestAdapter(CommonParamsAdapter commonParamsAdapter) {
        httpClient.setCommonParamsAdapter(commonParamsAdapter);
        return this;
    }

    public OkHttpClient.Builder getOkHttpBuilder() {
        return httpClient.builder();
    }

    public T build() {
        httpClient.build();

        Scheduler observeOn = AndroidSchedulers.mainThread();
        if (!mIsSubscribeOnUiThread) {
            observeOn = Schedulers.computation();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.getClient())
                .addCallAdapterFactory(new ObserveOnMainCallAdapterFactory(observeOn))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();

        T api = retrofit.create(cls);
        return api;
    }

    static final class ObserveOnMainCallAdapterFactory extends CallAdapter.Factory {
        final Scheduler scheduler;

        ObserveOnMainCallAdapterFactory(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            if (getRawType(returnType) != Observable.class) {
                return null; // Ignore non-Observable types.
            }
            final CallAdapter<Object, Observable<?>> delegate =
                    (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this, returnType,
                            annotations);

            return new CallAdapter<Object, Object>() {
                @Override
                public Object adapt(Call<Object> call) {
                    // Delegate to get the normal Observable...
                    final Observable<?> o = delegate.adapt(call);

                    return CacheStrategyUtil.handleSpecialCache(o.observeOn(scheduler), call);
                }

                @Override
                public Type responseType() {
                    return delegate.responseType();
                }
            };
        }
    }
}