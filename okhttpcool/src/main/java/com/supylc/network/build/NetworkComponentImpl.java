package com.supylc.network.build;

import com.supylc.network.NetworkComponent;
import com.supylc.network.internal.OKClientWrapper;
import com.supylc.network.support.ApiBuilder;

import okhttp3.OkHttpClient;

/**
 * @author Roye
 * @date 2018/11/13
 */
public class NetworkComponentImpl implements NetworkComponent {

    public NetworkComponentImpl() {
        ApiUtil.init();
    }

    @Override
    public NetworkBuilder builder(String group) {
        NetworkBuilder builder = ApiUtil.getBuilder(group);
        if (builder == null) {
            ApiBuilder apiBuilder = new ApiBuilder();
            builder = NetworkBuilder.with(apiBuilder);
            ApiUtil.setBuilder(group, builder);
        }
        return builder;
    }

    @Override
    public <T> NetworkBuilder builder(final Class<T> apiClass) {
        ApiGroup apiGroup = apiClass.getAnnotation(ApiGroup.class);
        NetworkBuilder builder = null;

        if (apiGroup != null) {
            builder = ApiUtil.getBuilder(apiGroup.group());
        }
        if (builder == null) {
            ApiBuilder<T> apiBuilder = new ApiBuilder();
            builder = NetworkBuilder.with(apiBuilder);
            builder.apiClass(apiClass);
        }

        return builder;
    }

    @Override
    public <T> T api(Class<T> apiClass) {
        T api = (T) ApiUtil.getApi(apiClass.getName());
        if (api == null) {
            ApiGroup apiGroup = apiClass.getAnnotation(ApiGroup.class);
            if (apiGroup != null) {
                NetworkBuilder builder = ApiUtil.getBuilder(apiGroup.group());
                if (builder == null) {
                    throw new RuntimeException(String.format("the apiGroup '%s' is not inited", apiGroup.group()));
                }
                api = builder.apiClass(apiClass).build();
                return api;
            }
            if (api == null) {
                throw new RuntimeException(String.format("this api '%s' is not inited", apiClass.getName()));
            }
        }

        return api;
    }

    private static OkHttpClient mClient;

    @Override
    public OkHttpClient getClient() {
        if (mClient == null) {
            synchronized (this) {
                if (mClient == null) {
                    OKClientWrapper clientWrapper = new OKClientWrapper();
                    clientWrapper.build();
                    mClient = clientWrapper.getClient();
                }
            }
        }
        return null;
    }

}
