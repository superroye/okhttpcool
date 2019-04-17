package com.supylc.network.build;

import android.support.v4.util.ArrayMap;

/**
 * @author Roye
 * @date 2019/2/26
 */
 class ApiUtil {

    private static ArrayMap<String, Object> apis;
    private static ArrayMap<String, NetworkBuilder> builderMap;

    public static void init() {
        apis = new ArrayMap<>(16);
        builderMap = new ArrayMap<>(16);
    }

    public static void setApi(String apiKey, Object api) {
        apis.put(apiKey, api);
    }

    public static Object getApi(String apiKey) {
        return apis.get(apiKey);
    }

    public static void setBuilder(String group, NetworkBuilder builder) {
        builderMap.put(group, builder);
    }

    public static NetworkBuilder getBuilder(String group) {
        return builderMap.get(group);
    }
}
