package com.mengdd.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class LogUtils {

    private static boolean DEBUG = true;
    private static String LOG_TAG = "mengdd";

    private final static boolean LOG_TO_FILE = false;
    private final static String LOG_FILE_DEFAULT_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/mengdd_debug_log.txt";
    private final static String LOG_FILE_PATH_ONE = Environment
            .getExternalStorageDirectory().getPath() + "/mengdd_time.txt";
    private final static String LOG_FILE_PATH_TWO = Environment
            .getExternalStorageDirectory().getPath() + "/mengdd_angle.txt";

    private static File LOG_FILE_DEFAULT;
    private static File LOG_FILE_ONE;
    private static File LOG_FILE_TWO;

    public static void footPrint() {
        if (DEBUG) {
            String className = Thread.currentThread().getStackTrace()[3]
                    .getClassName();
            int index = className.lastIndexOf(".");
            if (index > -1) {
                className = className.substring(index + 1);
            }
            String msgToPrint = Thread.currentThread().getId() + " "
                    + className + "."
                    + Thread.currentThread().getStackTrace()[3].getMethodName();
            println(Log.DEBUG, LOG_TAG, msgToPrint);
        }
    }

    public static void footPrint(String tag) {
        if (DEBUG) {
            String msgToPrint = Thread.currentThread().getStackTrace()[3]
                    .getMethodName();
            println(Log.DEBUG, tag, msgToPrint);
        }
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            println(Log.VERBOSE, tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            println(Log.VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            String className = Thread.currentThread().getStackTrace()[3]
                    .getClassName();
            int index = className.lastIndexOf(".");
            if (index > -1) {
                className = className.substring(index + 1);
            }

            String msgToPrint = Thread.currentThread().getId() + " "
                    + className + "."
                    + Thread.currentThread().getStackTrace()[3].getMethodName();
            if (!TextUtils.isEmpty(msg)) {
                msgToPrint += "--" + msg;
            }
            println(Log.DEBUG, LOG_TAG, msgToPrint);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            String msgToPrint = Thread.currentThread().getStackTrace()[3]
                    .getMethodName();
            if (!TextUtils.isEmpty(msg)) {
                msgToPrint += "--" + msg;
            }
            println(Log.DEBUG, tag, msgToPrint);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            println(Log.DEBUG, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            String className = Thread.currentThread().getStackTrace()[3]
                    .getClassName();
            int index = className.lastIndexOf(".");
            if (index > -1) {
                className = className.substring(index + 1);
            }

            String msgToPrint = Thread.currentThread().getId() + " "
                    + className + "."
                    + Thread.currentThread().getStackTrace()[3].getMethodName();
            if (!TextUtils.isEmpty(msg)) {
                msgToPrint += "--" + msg;
            }
            println(Log.INFO, LOG_TAG, msgToPrint);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            String msgToPrint = Thread.currentThread().getStackTrace()[3]
                    .getMethodName();
            msgToPrint += "--" + msg;
            println(Log.INFO, tag, msgToPrint);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            println(Log.INFO, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static int w(String tag, String msg) {
        return println(Log.WARN, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return println(Log.WARN, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(String tag, Throwable tr) {
        return println(Log.WARN, tag, getStackTraceString(tr));
    }

    public static int e(String msg) {
        return println(Log.ERROR, LOG_TAG, msg);
    }

    public static int e(String tag, String msg) {
        return println(Log.ERROR, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return println(Log.ERROR, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int wtf(String tag, String msg) {
        return Log.wtf(tag, msg, null);
    }

    public static int wtf(String tag, Throwable tr) {
        return Log.wtf(tag, tr.getMessage(), tr);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return Log.wtf(tag, msg, tr);
    }

    public static int println(int priority, String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (LOG_TO_FILE) {
            logToFile(tag, msg);
            return 0;
        }
        else {
            return Log.println(priority, tag, msg);
        }
    }

    // 如果没有指定路径则存在默认的文件中
    public static synchronized void logToFile(String tag, String msg) {
        try {
            if (LOG_FILE_DEFAULT == null) {
                LOG_FILE_DEFAULT = new File(LOG_FILE_DEFAULT_PATH);
                if (LOG_FILE_DEFAULT.exists()) {
                    LOG_FILE_DEFAULT.delete();
                }
                LOG_FILE_DEFAULT.createNewFile();
            }

            logToFile(LOG_FILE_DEFAULT, tag, msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logToFileOne(String msg) {
        try {
            if (LOG_FILE_ONE == null) {
                LOG_FILE_ONE = new File(LOG_FILE_PATH_ONE);
                if (LOG_FILE_ONE.exists()) {
                    LOG_FILE_ONE.delete();
                }
                LOG_FILE_ONE.createNewFile();
            }

            logToFile(LOG_FILE_ONE, null, msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logToFileTwo(String msg) {
        try {
            if (LOG_FILE_TWO == null) {
                LOG_FILE_TWO = new File(LOG_FILE_PATH_TWO);
                if (LOG_FILE_TWO.exists()) {
                    LOG_FILE_TWO.delete();
                }
                LOG_FILE_TWO.createNewFile();
            }

            logToFile(LOG_FILE_TWO, null, msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void logToFile(File logFile, String tag,
            String msg) {
        try {
            FileOutputStream outputStream = new FileOutputStream(logFile, true);

            StringBuilder builder = new StringBuilder();
            if (null == tag) {
                builder.append(msg).append("\n");
            }
            else {
                builder.append(tag).append("  :  ").append(msg).append("\n");
            }

            outputStream.write(builder.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
