package com.vince.upgrade;

import android.app.Application;
import android.content.Context;

import com.vince.upgrade.update.FFAppUpdateManager;

/**
 * Created by by tianweixin on 2019/4/1.
 */
public class AppUpgrade {
    public static final int STATUS_DOWNLOAD_START = 0;
    public static final int STATUS_DOWNLOAD_PROGRESS = 1;
    public static final int STATUS_DOWNLOAD_FINISH = 2;
    public static final int STATUS_DOWNLOAD_ERROR = 3;
    public static final int STATUS_INSTALLING = 4;
    public static final int STATUS_INSTALL_ERROR = 5;

    public static AppUpgrade instance;
    private Application appContext;

    private UpgradeProgressListener progressListener;

    private boolean showNotification = true;

    public static AppUpgrade getInstance() {
        if (instance == null) {
            instance = new AppUpgrade();
        }
        return instance;
    }

    private AppUpgrade() {
    }

    public void initialize(Application appContext) {
        this.appContext = appContext;
    }

    public Application getAppContext() {
        return appContext;
    }

    public void setUpgradeProgressListener(UpgradeProgressListener listener) {
        this.progressListener = listener;
    }

    public UpgradeProgressListener getProgressListener() {
        return progressListener;
    }

    public void upgrade(String apkUrl) {
        FFAppUpdateManager.getInstance().updateAndroid(apkUrl, null);
    }

    public void upgrade(String apkUrl, String md5) {
        FFAppUpdateManager.getInstance().updateAndroid(apkUrl, md5);
    }

    public void showNotification(boolean show) {
        showNotification = show;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void showVersionDialog(Context context, final String url, final String md5, String versionInfo, boolean isForce) {
        FFAppUpdateManager.getInstance().showVersionDialog(context, url, md5, versionInfo, isForce);
    }

    public void dismissAndClearDialog() {
        FFAppUpdateManager.getInstance().dismissAndClearDialog();
    }

    public boolean isVersionDialogShowing() {
        return FFAppUpdateManager.getInstance().isVersionDialogShowing();
    }

}
