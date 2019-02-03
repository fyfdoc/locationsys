package com.dtm.locationsys.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences 帮助类.
 */

public class SharedPrefHelper {

    /**
     * 保存登录用户信息
     * @param ctx
     * @param key
     * @param value
     */
    public static void setLoginInfo(Context ctx, String key, String value) {
        SharedPreferences setting = ctx.getSharedPreferences(
                Constants.LOGIN_FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = setting.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取保存的登录用户信息
     * @param ctx
     * @param key
     * @return
     */
    public static String getLoginInfo(Context ctx, String key) {
        SharedPreferences setting = ctx.getSharedPreferences(
                Constants.LOGIN_FILE_NAME, Context.MODE_PRIVATE);
        String value = setting.getString(key, "");
        return value;
    }

    /**
     * 保存网络配置信息
     * @param ctx
     * @param key
     * @param value
     */
    public static void setNetworkInfo(Context ctx, String key, String value) {
        SharedPreferences setting = ctx.getSharedPreferences(
                Constants.NETWORK_INFO_FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = setting.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取网络配置信息
     * @param ctx
     * @param key
     * @return
     */
    public static String getNetworkInfo(Context ctx, String key) {
        SharedPreferences setting = ctx.getSharedPreferences(
                Constants.NETWORK_INFO_FILE_NAME, Context.MODE_PRIVATE);
        String value = setting.getString(key, "");
        return value;
    }



}
