package com.vince.upgrade.tools;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.vince.upgrade.AppUpgrade;
import com.vince.upgrade.base.FFCustomToast;

/**
 * Created by by tianweixin on 2018/5/16.
 */

public class FFToastUtils {
    private static FFCustomToast toast;

    public static void showToast(String text) {
        Context context = AppUpgrade.getInstance().getAppContext();
        if (context != null) {
            showToast(context, text);
        }
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration) {
        getToast(context, text, duration).show();
    }

    public static void showToast(Context context, int textId, int duration) {
        getToast(context, textId, duration).show();
    }

    private static Toast getToast(Context context, String text, int duration) {
        if (toast != null)
            toast.cancel();

        toast = FFCustomToast.makeText(context, text, duration);
        return toast;
    }

    private static Toast getToast(Context context, int textId, int duration) {
        if (toast != null)
            toast.cancel();

        toast = FFCustomToast.makeText(context, textId, duration);
        return toast;
    }

}
