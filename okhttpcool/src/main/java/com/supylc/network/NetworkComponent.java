package com.supylc.network;

import com.supylc.network.build.NetworkBuilder;

import okhttp3.OkHttpClient;

/**
 * @author Roye
 * @date 2018/11/12
 */
public interface NetworkComponent {

    /**
     * @param group apiGroup初始化模式，多个api关联一个group初始化
     *              预初始化apiGroup，每个apiGroup, 对应一种初始化，baseurl，公参，cookie，签名这些
     *              定义Api时指定ApiGroup，就能关联预初始化的apiGroup
     */
    NetworkBuilder builder(String group);

    /**
     * @param apiClass api初始化模式，初始化一对一
     */
    <API> NetworkBuilder builder(Class<API> apiClass);

    /**
     * @param apiClass 通过类get到api入口，进行远程http接口调用
     */
    <API> API api(Class<API> apiClass);

    OkHttpClient getClient();
}
