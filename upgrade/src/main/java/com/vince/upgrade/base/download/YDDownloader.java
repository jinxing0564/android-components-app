package com.vince.upgrade.base.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.vince.upgrade.tools.FFLogger;

import java.io.File;

/**
 * Created by by tianweixin on 2018/5/17.
 */
public class YDDownloader {
    private Handler mainHandler;
    private DownloadListener downloadListener;
    private boolean isDownloading;
    private File downFile = null;

    public YDDownloader() {
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DownloadThread.MSG_START:
                        if (downloadListener != null) {
                            downloadListener.onStart();
                        }
                        break;
                    case DownloadThread.MSG_PROGRESS:
                        if (downloadListener != null) {
                            downloadListener.onProgress(msg.arg1);
                        }
                        break;
                    case DownloadThread.MSG_FINISH:
                        if (downloadListener != null) {
                            downloadListener.onFinish(downFile);
                        }
                        isDownloading = false;
                        downFile = null;
                        break;

                    case DownloadThread.MSG_ERROR:
                        if (downloadListener != null) {
                            downloadListener.onError();
                        }
                        isDownloading = false;
                        downFile = null;
                        break;
                    default:
                        FFLogger.e("do not recognize message");
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void startDownload(String url, File saveFile, DownloadListener downloadListener) {
        if (isDownloading) {
            FFLogger.d("当前已经有下载任务了");
            return;
        }
        this.downloadListener = downloadListener;
        downFile = saveFile;
        new DownloadThread(url, saveFile, mainHandler).start();
        isDownloading = true;
    }

    public interface DownloadListener {
        void onStart();

        /**
         * progress 0--100
         */
        void onProgress(int progress);

        void onFinish(File downFile);

        void onError();

    }
}
