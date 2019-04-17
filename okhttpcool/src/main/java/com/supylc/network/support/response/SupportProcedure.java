package com.supylc.network.support.response;

import android.text.TextUtils;
import android.util.Log;

import com.supylc.mobilearch.rxjava2.RxSimple;
import com.supylc.network.R;
import com.supylc.network.Utils;
import com.supylc.network.util.ILoadingDialog;
import com.supylc.network.util.NetworkUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.HttpException;

/**
 * @author Roye
 * @date 2018/9/11
 */
public class SupportProcedure<Result extends IHttpResponse, Data> {

    WeakReference<ILoadingDialog> mLoading;
    private String loadingText;
    private SupportResponseLifecycle<Result, Data> responseLifecycle;

    public SupportProcedure() {

    }

    public void setResponseLifecycle(SupportResponseLifecycle responseLifecycle) {
        this.responseLifecycle = responseLifecycle;
    }

    public void setLoading(ILoadingDialog loading) {
        setLoading(loading, Utils.getApp().getString(R.string.sdk_net_request_tips));
    }

    public void setLoading(ILoadingDialog loading, String loadingText) {
        if (loading != null) {
            this.mLoading = new WeakReference<>(loading);
            this.loadingText = loadingText;
        }
    }

    public void handle(Throwable e) {
        if (e != null) {
            String message;
            if (!NetworkUtils.isNetworkAvailable()) {
                message = Utils.getApp().getString(R.string.sdk_net_broken_tips);
            } else {
                message = e.getMessage();
                if (e instanceof IOException) {
                    message = Utils.getApp().getString(R.string.sdk_net_timeout_tips);
                } else if (e instanceof HttpException) {
                    message = Utils.getApp().getString(R.string.sdk_net_server_error_tips, ((HttpException) e).code());
                }
                if (TextUtils.isEmpty(message)) {
                    message = Utils.getApp().getString(R.string.sdk_net_link_error_tips);
                }
            }
            final String showMessage = message;
            //ToastUtils.showToast(showMessage);
        }
        Log.e("okhttp", "error", e);
    }

    public void handleResponse(Result result) {
        if (result != null) {
            if (result.isOk())
                responseLifecycle.onResponse((Data) result.getData());
            else
                responseLifecycle.onFailed(result);
        } else {
            responseLifecycle.onFailed(null);
        }
    }

    public void showLoading() {
        if (!TextUtils.isEmpty(loadingText) && mLoading != null) {
            RxSimple.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ILoadingDialog progress = mLoading.get();
                    if (progress != null) {
                        progress.showLoading(loadingText);
                    }
                }
            });
        }
    }

    public void hideLoading() {
        if (!TextUtils.isEmpty(loadingText) && mLoading != null) {
            RxSimple.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ILoadingDialog progress = mLoading.get();
                    if (progress != null) {
                        progress.hideLoading();
                    }
                }
            });
        }
    }

}
