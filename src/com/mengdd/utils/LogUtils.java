package com.mengdd.utils;

import android.util.Log;

public class LogUtils {

    private static boolean debug = true;

    public static String getTrackInfo() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        String className = element.getClassName();
        String method = element.getMethodName();
        return className + "-->" + method;
    }

    public static void v(String tag, String msg) {
        if (debug) {
            Log.v(tag, msg);
        }
    }

    public static void v(String msg) {
        v(getTrackInfo(), msg);
    }

    public static void d(String tag, String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {

        d(getTrackInfo(), msg);

    }

    public static void i(String tag, String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg) {
        i(getTrackInfo(), msg);

    }

    public static void w(String tag, String msg) {
        if (debug) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg) {
        w(getTrackInfo(), msg);
    }

    public static void e(String tag, String msg) {
        if (debug) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        e(getTrackInfo(), msg);
    }
}
