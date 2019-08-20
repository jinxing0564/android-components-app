package com.vince.upgrade.tools;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by tianweixin on 18/05/14.
 */
public class PostUtil {

    static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable){
        mainHandler.post(runnable);
    }

    public static void postDelayed(Runnable r, long delayMillis){
        mainHandler.postDelayed(r, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        mainHandler.removeCallbacks(runnable);
    }
}
