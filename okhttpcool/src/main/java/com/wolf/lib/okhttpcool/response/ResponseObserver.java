package com.wolf.lib.okhttpcool.response;

import com.wolf.lib.okhttpcool.listener.IRxObserveDisposer;

public abstract class ResponseObserver<Result> extends BaseResponseObserver<Result, Result> {

    public ResponseObserver() {
        this(null);
    }

    public ResponseObserver(IRxObserveDisposer rxDisposer) {
        super(rxDisposer);
    }

    @Override
    public void onFailed(Result t) {

    }
}
