package com.vince.upgrade.base.download;

import android.os.Handler;
import android.os.Message;

import com.vince.upgrade.tools.FFLogger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by by tianweixin on 2018/5/17.
 */
public class DownloadThread extends Thread {

    public static final int MSG_START = 1;
    public static final int MSG_PROGRESS = 2;
    public static final int MSG_FINISH = 3;
    public static final int MSG_ERROR = 4;

    private String sUrl;
    private File outFile;
    private Handler mHandler;

    public DownloadThread(String url, File outFile, Handler handler) {
        this.sUrl = url;
        this.outFile = outFile;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        FFLogger.i("download thread execute. url = " + sUrl);
        FileOutputStream fileOutputStream = null;
        InputStream inputStream;
        BufferedInputStream bufferedInputStream = null;
        //
        try {
            URL url = new URL(sUrl);
            //获取连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Connection", "Keep-Alive");
//            connection.setRequestProperty("Charset", "UTF-8");
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setUseCaches(false);
            //打开连接
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                sendMessage(MSG_START);
                FFLogger.i("download execute");

                inputStream = connection.getInputStream();
                bufferedInputStream = new BufferedInputStream(inputStream);
                fileOutputStream = new FileOutputStream(outFile);
                int contentLength = connection.getContentLength();
                byte[] bytes = new byte[1024];
                long totalReaded = 0;
                int temp_Len;
                int mProgress = -1;
                while ((temp_Len = bufferedInputStream.read(bytes)) != -1) {
                    totalReaded += temp_Len;
                    int progress = (int) (totalReaded * 100 / contentLength);
                    fileOutputStream.write(bytes, 0, temp_Len);
                    if (progress > mProgress) {
                        sendMessage(MSG_PROGRESS, progress);
                        mProgress = progress;
                    }
                }

                sendMessage(MSG_FINISH);
                FFLogger.i("download finish");
            } else {
                throw new RuntimeException("url can't connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(MSG_ERROR);
            FFLogger.e("download failed");
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendMessage(int what, int... arg1) {
        Message msg = Message.obtain();
        msg.what = what;
        if (arg1 != null && arg1.length > 0) {
            msg.arg1 = arg1[0];
        }
        mHandler.sendMessage(msg);
    }

}
