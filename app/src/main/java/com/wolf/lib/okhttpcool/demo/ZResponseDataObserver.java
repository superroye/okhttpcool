package com.wolf.lib.okhttpcool.demo;

import com.supylc.mobilearch.rxjava2.IRxObserveDisposer;
import com.supylc.network.support.response.BaseResponseObserver;

/**
 * @author Roye
 * @date 2018/9/26
 */
public abstract class ZResponseDataObserver<Data> extends BaseResponseObserver<ZHttpResponse<Data>, Data> {

    public ZResponseDataObserver(IRxObserveDisposer rxDisposer) {
        super(rxDisposer);
    }

    @Override
    public void onFailed(ZHttpResponse<Data> result) {

    }
}
