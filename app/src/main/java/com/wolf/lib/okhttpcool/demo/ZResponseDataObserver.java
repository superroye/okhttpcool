package com.wolf.lib.okhttpcool.demo;

import com.wolf.lib.okhttpcool.listener.IRxObserveDisposer;
import com.wolf.lib.okhttpcool.response.BaseResponseObserver;

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
