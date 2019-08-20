package com.vince.upgrade.tools;

import android.util.Log;

public class FFLogger {

    public static boolean LOG_ENABLED = false;

    private static final String TAG = "FFUpgrade";

    private FFLogger() {
    }

    public static boolean isLoggable(int level) {
        return Log.isLoggable(TAG, level);
    }

    public static String getStackTraceString(Throwable th) {
        return Log.getStackTraceString(th);
    }

    public static void printStackTrace(String description, Throwable th) {
        if (LOG_ENABLED)
            Log.e(TAG, description + "\n" + getStackTraceString(th));
    }

    public static void v(String msg) {
        if (LOG_ENABLED)
            Log.v(TAG, msg);
    }

    public static void v(String msg, Throwable th) {
        if (LOG_ENABLED)
            Log.v(TAG, msg, th);
    }

    public static void d(String msg) {
        if (LOG_ENABLED)
            Log.d(TAG, msg);
    }

    public static void d(String msg, Throwable th) {
        if (LOG_ENABLED)
            Log.d(TAG, msg, th);
    }

    public static void i(String msg) {
        if (LOG_ENABLED)
            Log.i(TAG, msg);
    }

    public static void i(String msg, Throwable th) {
        if (LOG_ENABLED)
            Log.i(TAG, msg, th);
    }

    public static void w(String msg) {
        if (LOG_ENABLED)
            Log.w(TAG, msg);
    }

    public static void w(String msg, Throwable th) {
        if (LOG_ENABLED)
            Log.w(TAG, msg, th);
    }

    public static void w(Throwable th) {
        if (LOG_ENABLED)
            Log.w(TAG, th);
    }

    public static void e(String msg) {
        if (LOG_ENABLED)
            Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable th) {
        if (LOG_ENABLED)
            Log.e(TAG, msg, th);
    }

}
