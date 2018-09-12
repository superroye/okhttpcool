package com.wolf.lib.okhttpcool.demo;

import com.wolf.lib.okhttpcool.ApiBuilder;
import com.wolf.lib.okhttpcool.support.GlobalRequestAdapter;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class ZZService {

    private static ZZCAPi zzcaPi;

    public static ZZCAPi api() {
        if (zzcaPi == null) {
            synchronized (ZZService.class){
                if(zzcaPi == null){
                    init();
                }
            }
        }
        return zzcaPi;
    }

    private static void init() {
        ApiBuilder<ZZCAPi> apiBuilder = new ApiBuilder<ZZCAPi>() {
        };
        apiBuilder.setGlobalRequestAdapter(new GlobalRequestAdapter() {
            @Override
            public void addHeader(Request.Builder builder) {
                //builder.addHeader("head1","value");
            }

            @Override
            public void addQueryParams(HttpUrl.Builder builder) {
                //builder.addEncodedQueryParameter("test","testName");
            }

            @Override
            public void addPostParams(FormBody.Builder builder) {
                //builder.add("testPost", "testPostValue");
            }
        });
        apiBuilder.baseUrl("https://suggest.taobao.com/");
        zzcaPi = apiBuilder.build();
    }
}
