package com.wolf.lib.okhttpcool.demo;

import com.wolf.lib.okhttpcool.response.IHttpResponse;
import java.io.Serializable;

/**
 * @author Roye
 * @date 2018/9/26
 */
public class ZHttpResponse<Data> implements IHttpResponse<Data>, Serializable {

    public String code;
    public String message;
    public Data data;

    @Override
    public boolean isOk() {
        return "0".equals(code) || "200".equals(code);
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public Data getData() {
        return data;
    }
}
