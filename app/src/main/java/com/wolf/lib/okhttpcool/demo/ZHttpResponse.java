package com.wolf.lib.okhttpcool.demo;

import com.supylc.network.support.response.IHttpResponse;

import java.io.Serializable;

/**
 * @author Roye
 * @date 2018/9/26
 */
public class ZHttpResponse<Data> implements IHttpResponse<Data>, Serializable {

    public String code;
    public String message;
    public Data result;

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public Data getData() {
        return result;
    }
}
