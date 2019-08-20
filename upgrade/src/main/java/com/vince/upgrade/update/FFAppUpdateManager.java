package com.vince.upgrade.update;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;


import com.vince.upgrade.AppUpgrade;
import com.vince.upgrade.R;
import com.vince.upgrade.base.FFFileProvider;
import com.vince.upgrade.base.download.YDDownloader;
import com.vince.upgrade.tools.FFLogger;
import com.vince.upgrade.tools.FFToastUtils;
import com.vince.upgrade.tools.MD5Util;
import com.vince.upgrade.tools.UrlUtils;

import java.io.File;

/**
 * Created by by tianweixin on 2018/10/25.
 */
public class FFAppUpdateManager {
    private static FFAppUpdateManager instance;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int NOTIFY_ID = 32;
    private int mProgress = -1;
    private ManagerState mState = ManagerState.IDLE;
    private String apkMD5;

    private Context dialogContext;
    private FFVersionDialog mDialog;
    private int downloadProgress = 0;

    public static FFAppUpdateManager getInstance() {
        if (instance == null) {
            instance = new FFAppUpdateManager();
        }
        return instance;
    }

    public boolean isVersionDialogShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public void showVersionDialog(Context context, final String url, final String md5, String versionInfo, boolean isForce) {
        this.dialogContext = context;

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mDialog = new FFVersionDialog(dialogContext);
        mDialog.setTitle("有版本可以更新啦");
        mDialog.setCancelable(!isForce);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismissAndClearDialog();
            }
        });
        mDialog.setMessage(versionInfo);
        String leftText;
        if (isForce) {
            leftText = null;
        } else {
            leftText = "下次再说";
        }
        mDialog.setButtons(leftText, 0, "立即更新", 0, new FFVersionDialog.ButtonClickListener() {
            @Override
            public void onLeftClick() {
                dismissAndClearDialog();
            }

            @Override
            public void onRightClick() {
                updateAndroid(url, md5);
            }
        });
        mDialog.show();
        if (mState == ManagerState.DOWNLOADING) {
            mDialog.updateProgress(downloadProgress);
        }
        dialogContext = null;
    }

    public void dismissAndClearDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
        dialogContext = null;
    }

    public void updateAndroid(String url, String md5) {
        this.apkMD5 = md5;
        downloadNewApk(url);
    }

    private void downloadNewApk(String url) {
        if (mState == ManagerState.DOWNLOADING) {
            FFLogger.d("已经在download");
            return;
        }
        if (!UrlUtils.isHttpUrl(url)) {
            FFLogger.e("App Update url not valid");
            FFToastUtils.showToast("下载url不正确");
            return;
        }

        Application app = AppUpgrade.getInstance().getAppContext();
        String strPath = app.getExternalFilesDir(null) + File.separator + getApkName(url);
        File apkFile = new File(strPath);
        if (apkFile.exists()) {
            apkFile.delete();
        }
        new YDDownloader().startDownload(url, apkFile, downloadListener);
        mState = ManagerState.DOWNLOADING;
    }

    private void notifyStateProgress(int status, int progress) {
        if (AppUpgrade.getInstance().getProgressListener() != null) {
            AppUpgrade.getInstance().getProgressListener().onProgress(status, progress);
        }
        if (mDialog != null && mDialog.isShowing()) {
            if (status == AppUpgrade.STATUS_DOWNLOAD_START || status == AppUpgrade.STATUS_DOWNLOAD_PROGRESS) {
                mDialog.updateProgress(progress);
            } else {
                mDialog.showProgress(false);
            }
        }
    }

    YDDownloader.DownloadListener downloadListener = new YDDownloader.DownloadListener() {
        @Override
        public void onStart() {
            downloadProgress = 0;
            updateNotification(0);
            notifyStateProgress(AppUpgrade.STATUS_DOWNLOAD_START, 0);
        }

        @Override
        public void onProgress(int progress) {
            FFLogger.d("onProgress" + progress);
            downloadProgress = progress;
            notifyStateProgress(AppUpgrade.STATUS_DOWNLOAD_PROGRESS, progress);
            updateNotification(progress);
        }

        @Override
        public void onFinish(File downFile) {
            mState = ManagerState.IDLE;
            dismissNotification();
            downloadProgress = 0;
            notifyStateProgress(AppUpgrade.STATUS_DOWNLOAD_FINISH, 100);
            installApk(downFile);
        }

        @Override
        public void onError() {
            mState = ManagerState.IDLE;
            FFToastUtils.showToast("下载失败，请稍候重试");
            cleanDownloadInstallation();
            downloadProgress = 0;
            notifyStateProgress(AppUpgrade.STATUS_DOWNLOAD_ERROR, 0);
        }
    };


    private void initNotification() {
        Context context = AppUpgrade.getInstance().getAppContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        builder = new NotificationCompat.Builder(context, null)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_upgrade)
                .setOnlyAlertOnce(true)
                .setContentTitle("正在下载");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "1002";
            String channelName = "app_download_apk";
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }
        FFLogger.d("Notify init");
    }

    private void updateNotification(int progress) {
        if (!AppUpgrade.getInstance().isShowNotification()) {
            return;
        }
        if (mProgress < progress) {
            if (builder == null) {
                initNotification();
            }
            if (builder == null) {
                return;
            }
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
            notificationManager.notify(NOTIFY_ID, builder.build());
            FFLogger.d("notify process = " + progress);
        }
        mProgress = progress;
    }

    private void dismissNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFY_ID);
            builder = null;
            notificationManager = null;
        }
    }

    private void installApk(File apkFile) {
        String fileMd5 = MD5Util.getMD5OfFile(apkFile);
        if (apkFile == null || !apkFile.exists() || (!TextUtils.isEmpty(apkMD5) && !apkMD5.equalsIgnoreCase(fileMd5))) {
            FFToastUtils.showToast("安装文件不正确");
            cleanDownloadInstallation();
            notifyStateProgress(AppUpgrade.STATUS_INSTALL_ERROR, 0);
            return;
        }

        Context context = AppUpgrade.getInstance().getAppContext();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = AppUpgrade.getInstance().getAppContext().getPackageName() + ".fileprovider";
            Uri apkUri = FFFileProvider.getUriForFile(context, authority, apkFile);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(apkFile);
            i.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(i);
        notifyStateProgress(AppUpgrade.STATUS_INSTALLING, 0);
    }

    private static String getApkName(String url) {
        int index = url.lastIndexOf("/");
        if (index >= 0 && index + 1 < url.length()) {
            return url.substring(index + 1);
        }
        return AppUpgrade.getInstance().getAppContext().getPackageName() + ".apk";
    }

    private void cleanDownloadInstallation() {
        dismissNotification();
    }

    enum ManagerState {
        IDLE, CHECKING, DOWNLOADING;
    }

}
