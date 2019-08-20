package com.vince.upgrade.exclusionClick;

import android.view.View;

/**
 * Created by by tianweixin on 2018/10/25.
 */
public class ExclusionUtils {
    private static final long DOUBLE_CLICK_TIME = 500;

    private static long mLastClickTime;

    private static int mLastClickViewId;

    public static boolean isExclusionClick(View v) {
        return isExclusionClick(v, DOUBLE_CLICK_TIME);
    }

    /**
     * 是否是快速点击
     */
    public static boolean isExclusionClick(View v, long intervalMillis) {
        int viewId = v.getId();
        long time = System.currentTimeMillis();
        long timeInterval = Math.abs(time - mLastClickTime);
        if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
            return true;
        } else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }
}

