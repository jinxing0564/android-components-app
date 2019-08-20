package com.vince.upgrade.tools;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by tianweixin on 16/10/8.
 */
public class SystemBarUtil {

    public static void changeSystemBar(Window window, int barColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentStatus(window, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(window);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(barColor); // 使用颜色资源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    || (SystemRoomUtils.isFlyme() && !hasNavigationBar(tintManager))) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消半透明效果
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(barColor);
        }
    }

    public static int getStatusBarHeight(Window window) {
        SystemBarTintManager tintManager = new SystemBarTintManager(window);
        int height = tintManager.getStatusBarHeight();
        if (height <= 0) {
            height = ScreenUtil.dipToPixel(20);
        }
        return height;
    }

    public static void statusBarLightMode(Window window) {
        //6.0以下官方不支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            FFLogger.w("not support change status bar light mode below 6.0");
            return;
        }
        //Miui 9 以下，没有follow android标准做法，需要特殊处理
        if (SystemRoomUtils.getMiuiVersion() < 9 && setStatusBarLightMode_MIUI(window, true)) {
            return;
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static void statusBarDarkMode(Window window) {
        //6.0以下官方不支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            FFLogger.w("not support change status bar light mode below 6.0");
            return;
        }
        //Miui 9 以下，没有follow android标准做法，需要特殊处理
        if (SystemRoomUtils.getMiuiVersion() < 9 && setStatusBarLightMode_MIUI(window, false)) {
            return;
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private static boolean hasNavigationBar(SystemBarTintManager tintManager) {
        String hwKeys = SystemRoomUtils.getSystemProperty("qemu.hw.mainkeys");
        return tintManager.getConfig().hasNavigtionBar() || "0".equals(hwKeys);
    }

    private static boolean isEMUI3_1() {
        if ("EmotionUI_3.1".equals(getEmuiVersion())) {
            return true;
        }
        return false;
    }

    private static String getEmuiVersion() {
        Class<?> classType = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, "ro.build.version.emui");
        } catch (Exception e) {
            FFLogger.w(e);
        }
        return "";
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Window window, boolean on) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    static boolean isFullScreen(Window window) {
        WindowManager.LayoutParams attrs = window.getAttributes();

        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        }

        return false;
    }

    private static int getDpi(Window window) {
        int dpi = 0;
        Display display = window.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    private static boolean setStatusBarLightMode_MIUI(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

}
