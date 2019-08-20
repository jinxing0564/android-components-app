package com.vince.upgrade.update;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vince.upgrade.R;
import com.vince.upgrade.exclusionClick.FFOnClickListener;
import com.vince.upgrade.tools.ScreenUtil;

public class FFVersionDialog extends Dialog {
    private Context curContext;
    private boolean cancelable = true;

    private TextView tvTitle;
    private TextView tvMessage;
    private RelativeLayout btnLeft;
    private TextView tvLeft;
    private TextView btnRight;
    private ProgressBar progressBar;
    private View lytProgress, lytButtons;
    private TextView tvProgress;

    private ButtonClickListener mClickListener;

    public FFVersionDialog(Context context) {
        super(context);
        this.curContext = context;
        setContentView(R.layout.layout_version_dialog);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        lytButtons = findViewById(R.id.lyt_btns);
        btnLeft = (RelativeLayout) findViewById(R.id.btn_left);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        btnRight = (TextView) findViewById(R.id.btn_right);
        lytProgress = findViewById(R.id.lyt_progress);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        int dialogWidth = (int) (ScreenUtil.getScreenWidth(context) * 0.8f);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            if (lp != null) {
                lp.width = dialogWidth; //设置宽度
            } else {
                lp = new WindowManager.LayoutParams(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            window.setAttributes(lp);
        }

        initObserver();
        showProgress(false);
    }

    public void updateProgress(int progress) {
        showProgress(true);
        tvProgress.setText("正在下载 " + progress + "%");
        progressBar.setProgress(progress);
    }

    public void showProgress(boolean show) {
        lytProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        lytButtons.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    private void initObserver() {
        View.OnClickListener listener = new FFOnClickListener() {
            @Override
            public void onClicked(View v) {
                if (v.getId() == R.id.btn_left) {
                    if (mClickListener != null) {
                        mClickListener.onLeftClick();
                    }
                } else if (v.getId() == R.id.btn_right) {
                    if (mClickListener != null) {
                        mClickListener.onRightClick();
                    }
                }
            }
        };
        btnLeft.setOnClickListener(listener);
        btnRight.setOnClickListener(listener);

    }

    public Context getCurContext() {
        return curContext;
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
    }

    public void setMessage(String message) {
        tvMessage.setText(message);
    }

    public void setButtons(String leftText, int leftColorRes, String rightText, int rightColorRes, ButtonClickListener listener) {
        if (TextUtils.isEmpty(leftText)) {
            btnLeft.setVisibility(View.GONE);
        } else {
            btnLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(leftText);
            if (leftColorRes != 0) {
                tvLeft.setTextColor(curContext.getResources().getColor(leftColorRes));
            }
        }
        if (TextUtils.isEmpty(rightText)) {
            btnRight.setVisibility(View.GONE);
        } else {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setText(rightText);
            if (rightColorRes != 0) {
                btnRight.setTextColor(curContext.getResources().getColor(rightColorRes));
            }
        }
        mClickListener = listener;
    }


    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        this.setCanceledOnTouchOutside(this.cancelable);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (cancelable) {
            return super.onKeyDown(keyCode, event);
        } else {
            return false;
        }
    }

    public interface ButtonClickListener {
        void onLeftClick();

        void onRightClick();
    }


}
