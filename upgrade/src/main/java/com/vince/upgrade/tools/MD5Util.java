package com.vince.upgrade.tools;

/**
 * Created by dfqin on 2018/4/24.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by dfqin on 2017/3/20.
 */


public class MD5Util {

    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /*****
     * 获取md5 值
     * @param file
     * @return
     */
    public static String getMD5OfFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase(Locale.ENGLISH);//转为大写
            while (value.length() < 32) {
                value = "0" + value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static String to32Str(String sourceString) throws Exception {
        String upperStr = toHexString(toByte(sourceString.getBytes()));
        return upperStr.toLowerCase();
    }

    public static String to32Str(byte[] source) throws Exception {
        String upperStr = toHexString(toByte(source));
        return upperStr.toLowerCase();
    }


    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);

        for (int i = 0; i < b.length; ++i) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }

        return sb.toString();
    }

    private static byte[] toByte(byte[] source) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] messageDigest = digest.digest(source);
        return messageDigest;
    }

}
