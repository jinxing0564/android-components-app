package com.vince.upgrade.exclusionClick;

import android.view.View;

import com.vince.upgrade.tools.FFLogger;

/**
 * Created by by tianweixin on 2019/1/17.
 */
public abstract class FFOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (ExclusionUtils.isExclusionClick(v, 500)) {
            FFLogger.d("quick click!!!");
        } else {
            // 执行点击事件
            onClicked(v);
        }
    }

    public abstract void onClicked(View v);
}
