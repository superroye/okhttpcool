package com.wolf.lib.okhttpcool.response;

import com.wolf.lib.okhttpcool.cache.CacheStrategyUtil;
import com.wolf.lib.okhttpcool.listener.IProgressDialog;
import com.wolf.lib.okhttpcool.util.NetworkUtils;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Roye
 * @date 2018/9/6
 */
public abstract class PriorityCacheResponseDataCallback<Result extends IHttpResponse<Data>, Data> implements Callback<Result>, SupportResponseLifecycle<Result, Data> {

    private SupportProcedure procedure;
    private int requestCount;
    private okhttp3.Response lastCacheResopnse;

    public PriorityCacheResponseDataCallback() {
        procedure = new SupportProcedure();
        procedure.setResponseLifecycle(this);
    }

    public void setProgressDialog(IProgressDialog progressDialog) {
        procedure.setProgressDialog(progressDialog);
    }

    public void setProgressDialog(IProgressDialog progressDialog, String loadingText) {
        procedure.setProgressDialog(progressDialog, loadingText);
    }

    @Override
    public void onStart() {
        procedure.showLoading();
    }

    @Override
    public void onResponse(Call<Result> call, final Response<Result> response) {
        okhttp3.Response networkResopnse = response.raw().networkResponse();
        if (networkResopnse != null) {
            if (lastCacheResopnse == null) {
                procedure.handleResponse(response.body());
                onFinish();
            } else {
                CacheStrategyUtil.checkSame(lastCacheResopnse, networkResopnse, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if ("n".equals(s)) {
                            procedure.handleResponse(response.body());
                        }
                        onFinish();
                    }
                });
            }
        } else {
            lastCacheResopnse = response.raw().cacheResponse();

            procedure.handleResponse(response.body());
            onFinish();

            if (requestCount == 0) {
                requestCount++;
                if (NetworkUtils.isAvailable()) {
                    call.clone().enqueue(this);
                }
            }
        }
    }

    @Override
    public void onFinish() {
        procedure.hideLoading();
    }

    @Override
    public void onFailed(Result result) {
        procedure.doFailed(result);
    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        onError(t);
    }

    public void onError(Throwable e) {
        procedure.handle(e);
        onFinish();
    }
}
