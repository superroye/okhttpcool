package com.wolf.lib.okhttpcool.support;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author Roye
 * @date 2018/8/6
 */
public interface GlobalRequestAdapter {

    public void addHeader(Request.Builder builder);

    public void addQueryParams(HttpUrl.Builder httpUrlBuilder);
}
