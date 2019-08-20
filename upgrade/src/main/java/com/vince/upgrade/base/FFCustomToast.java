package com.vince.upgrade.base;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vince.upgrade.R;

/**
 * Created by by tianweixin on 2018/5/16.
 */
public class FFCustomToast extends Toast {

    public FFCustomToast(Context context) {
        super(context);
    }

    public static FFCustomToast makeText(Context context, String text, int duration) {
        FFCustomToast result = new FFCustomToast(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.upgrade_custom_toast, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);
        result.setView(v);
        result.setDuration(duration);
        result.setGravity(Gravity.CENTER, 0, 0);
        return result;
    }

    public static FFCustomToast makeText(Context context, int textId, int duration) {
        FFCustomToast result = new FFCustomToast(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.upgrade_custom_toast, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(textId);
        result.setView(v);
        result.setDuration(duration);
        result.setGravity(Gravity.CENTER, 0, 0);
        return result;
    }
}
