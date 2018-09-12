package com.wolf.lib.okhttpcool.cache;

/**
 * @author Roye
 * @date 2018/9/10
 * <p>
 * getcache，获取缓存，过期则请求网络
 * refresh，请求网络并刷新缓存，可配合getcache使用
 * getandrefresh，请求缓存，并请求网络，网络不一致则刷新UI
 */
public enum CacheStrategy {
    getcache, refresh, getandrefresh;

    @Override
    public String toString() {
        return super.name();
    }
}