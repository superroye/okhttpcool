package com.wolf.lib.okhttpcool.response;

/**
 * @author Roye
 * @date 2018/9/28
 */
public class DefaultResponseCodeHandle {

    public static void handle(IHttpResponse response) {
        HttpResponse result = (HttpResponse) response;
        if (result.code == 1 || result.code == 1003) {
            return;
        }

    }
}
