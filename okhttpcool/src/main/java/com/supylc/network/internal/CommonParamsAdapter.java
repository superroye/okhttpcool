package com.supylc.network.internal;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author Roye
 * @date 2018/8/6
 */
public interface CommonParamsAdapter {

    void addHeader(Request.Builder builder);

    void addQueryParams(Request originalRequest, HttpUrl.Builder httpUrlBuilder);

    void addPostParams(Request originalRequest, Request.Builder requestBuilder);
}
