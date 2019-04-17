package com.supylc.network.support.response;

/**
 * @author Roye
 * @date 2018/9/26
 */
public interface IHttpResponse<Data> {

    boolean isOk();

    String getMsg();

    Data getData();
}
