package com.wolf.lib.okhttpcool.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.wolf.lib.okhttpcool.util.UIUtils;

/**
 * @author Roye
 * @date 2018/9/11
 */
public class BaseApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    public static String DES_KEY = "sealgame_mutil";
    public static String APP_ID;
    public static String APP_UID;
    public static String APP_TOKEN;
    public static String APP_CHANNEL;
    public static String APP_VERSION;
    public static String APP_OS = "2";
    public static boolean APP_DEBUG;
    public static Application app;
    private static Activity resumeActivity;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private Runnable check;

    public static Activity getResumeActivity() {
        return resumeActivity;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        baseInit();
        registerActivityLifecycleCallbacks(this);
    }

    public void baseInit() {
        setChannel();

        APP_DEBUG = true;
        LibContext.setApp(this, APP_DEBUG);

        setEnv();

        UIUtils.init(this);
    }

    void setChannel() {
    }

    void setEnv() {

    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (resumeActivity == activity) {
            resumeActivity = null;
        }
    }

    protected void onAppUpdate(int versionCode, int oldVersionCode) {

    }
}