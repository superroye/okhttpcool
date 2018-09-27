package com.wolf.lib.okhttpcool.response;

/**
 * Created by Roye on 2016/12/8.
 */

public class HttpResponse<T> extends BaseResponse implements IHttpResponse {
    public T data;

    @Override
    public boolean isOk() {
        return code == 0;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return data;
    }

}
