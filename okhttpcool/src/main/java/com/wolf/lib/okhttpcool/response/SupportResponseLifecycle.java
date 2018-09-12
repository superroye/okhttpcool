package com.wolf.lib.okhttpcool.response;

/**
 * @author Roye
 * @date 2018/9/11
 */
public interface SupportResponseLifecycle<T> {

    public void onStart();

    public void onResponse(T result);

    public void onFailed(HttpResponse<T> result);

    public void onFinish();

}
