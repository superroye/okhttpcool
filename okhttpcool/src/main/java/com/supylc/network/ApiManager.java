package com.supylc.network;

import android.app.Application;

import com.supylc.network.build.NetworkBuilder;
import com.supylc.network.build.NetworkComponentImpl;

import okhttp3.OkHttpClient;

/**
 * @author Roye
 * @date 2018/11/13
 */
public class ApiManager {

    private static NetworkComponent mNetworkComponent;

    private static NetworkComponent component() {
        if (mNetworkComponent == null) {
            synchronized (ApiManager.class) {
                if (mNetworkComponent == null) {
                    mNetworkComponent = new NetworkComponentImpl();
                }
            }
        }
        return mNetworkComponent;
    }

    public static ApiInitBuilder with(Application app) {
        return new ApiInitBuilder(app);
    }

    public static void setDebug(boolean isDebug) {
        Utils.setDebug(isDebug);
    }

    public static <API> API api(Class<API> apiClass) {
        return component().api(apiClass);
    }

    public static OkHttpClient getClient() {
        return component().getClient();
    }

    public static class ApiInitBuilder {

        private ApiInitBuilder(Application app) {
            Utils.init(app);
        }

        public NetworkBuilder groupBuilder(String group) {
            return component().builder(group);
        }

        public <API> NetworkBuilder builder(Class<API> apiClass) {
            return component().builder(apiClass);
        }

    }
}
