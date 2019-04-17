package com.wolf.lib.okhttpcool.demo;

import android.app.Application;

import com.supylc.network.ApiManager;
import com.supylc.network.internal.CommonParamsAdapter;

import java.io.File;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author Roye
 * @date 2019/4/17
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ApiManager.setDebug(true);
        ApiManager.with(this)
                .groupBuilder("main")
                .baseUrl("https://suggest.taobao.com/")
                .cache(new Cache(new File(getCacheDir()+"/okhttpcache"), 64*1024*1024))
                .connectTimeout(10)
                .readTimeout(10)
                .requestAdapter(new CommonParamsAdapter() {
                    @Override
                    public void addHeader(Request.Builder builder) {

                    }

                    @Override
                    public void addQueryParams(Request originalRequest, HttpUrl.Builder httpUrlBuilder) {

                    }

                    @Override
                    public void addPostParams(Request originalRequest, Request.Builder requestBuilder) {

                    }
                })
                .build();
    }
}
