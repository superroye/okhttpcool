package com.wolf.lib.okhttpcool.response;

import com.wolf.lib.okhttpcool.listener.IRxObserveDisposer;

/**
 * Created by Roye on 2016/12/8.
 */

public abstract class ResponseDataObserver<T> extends BaseResponseObserver<HttpResponse<T>, T> {

    public ResponseDataObserver() {
        this(null);
    }

    /**
     * 非必传
     *
     * @param observeDisposer observeDisposer在fragment or activity实现，建议传此参数
     */
    public ResponseDataObserver(IRxObserveDisposer observeDisposer) {
        super(observeDisposer);
    }

    @Override
    public void onFailed(HttpResponse<T> result) {
        procedure.doFailed(result);
    }
}
