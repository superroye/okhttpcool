package com.supylc.network;

import android.app.Application;

/**
 * @author Roye
 * @date 2019/2/11
 */
public class Utils {

    private static Application application;
    private static boolean isDebug;

    public static void init(Application app) {
        Utils.application = app;
    }

    public static void setDebug(boolean isDebug) {
        Utils.isDebug = isDebug;
    }

    public static Application getApp() {
        return application;
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
