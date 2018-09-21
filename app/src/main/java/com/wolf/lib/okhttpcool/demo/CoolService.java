package com.wolf.lib.okhttpcool.demo;

import com.wolf.lib.okhttpcool.ApiBuilder;
import com.wolf.lib.okhttpcool.CoolHttpClient;
import com.wolf.lib.okhttpcool.support.GlobalRequestAdapter;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class CoolService {

    private static CoolAPi coolAPi;

    public static CoolAPi api() {
        if (coolAPi == null) {
            synchronized (CoolService.class) {
                if (coolAPi == null) {
                    init();
                }
            }
        }
        return coolAPi;
    }

    private static void init() {
        ApiBuilder<CoolAPi> apiBuilder = new ApiBuilder<CoolAPi>() {

            @Override
            public void onBuild(CoolHttpClient httpClient) {
                httpClient.setGlobalRequestAdapter(new GlobalRequestAdapter() {
                    @Override
                    public void addHeader(Request.Builder builder) {
                        //builder.addHeader("head1","value");
                    }

                    @Override
                    public void addQueryParams(HttpUrl.Builder builder) {
                        //builder.addEncodedQueryParameter("test","testName");
                    }
                });


                //httpClient.builder().cookieJar();
            }
        };
        apiBuilder.baseUrl("https://suggest.taobao.com/");

        coolAPi = apiBuilder.build();
    }
}
