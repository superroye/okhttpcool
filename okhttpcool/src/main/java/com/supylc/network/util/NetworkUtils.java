package com.supylc.network.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.supylc.network.Utils;

/**
 * create by elileo on 2019/2/18
 */
public class NetworkUtils {
    public static class NetworkType{
        public static final String UNKNOWN = "unknown";
        public static final String NET_2G =  "2G";
        public static final String NET_3G = "3G";
        public static final String NET_4G = "4G";
        public static final String WIFI = "wifi";
    }

    public static boolean isNetworkAvailable() {
        return !(NetworkType.UNKNOWN.endsWith(getNetworkType()));
    }

    public static String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo == null) {
            return NetworkType.UNKNOWN;
        }
        if(netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        }
        TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();
        if(netType == TelephonyManager.NETWORK_TYPE_GPRS || netType == TelephonyManager.NETWORK_TYPE_EDGE ||
                netType == TelephonyManager.NETWORK_TYPE_CDMA || netType == TelephonyManager.NETWORK_TYPE_1xRTT || netType == TelephonyManager.NETWORK_TYPE_IDEN) {
            return NetworkType.NET_2G;
        }else if(netType == TelephonyManager.NETWORK_TYPE_LTE){
            return NetworkType.NET_4G;
        }
        return NetworkType.NET_3G;
    }
}
