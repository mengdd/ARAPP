package com.mengdd.sina.weibo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 该类用于保存Oauth2AccessToken到sharedPreference，并提供读取功能
 * 
 * 
 */
public class AccessTokenKeeper {
    private static final String PREFERENCES_NAME = "pref_com_weibo_sdk_android";
    private static final String PREF_TOKEN = "access_token";
    private static final String PREF_EXPIRE = "expires_in";

    /**
     * 保存accesstoken到SharedPreferences
     * 
     * @param context
     *            Activity 上下文环境
     * @param token
     *            Oauth2AccessToken
     */
    public static void keepAccessToken(Context context, Oauth2AccessToken token) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(PREF_TOKEN, token.getToken());
        editor.putLong(PREF_EXPIRE, token.getExpiresTime());
        editor.commit();
    }

    /**
     * 清空sharepreference
     * 
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 从SharedPreferences读取accessstoken
     * 
     * @param context
     * @return Oauth2AccessToken
     */
    public static Oauth2AccessToken readAccessToken(Context context) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        token.setToken(pref.getString(PREF_TOKEN, ""));
        token.setExpiresTime(pref.getLong(PREF_EXPIRE, 0));
        return token;
    }
}
