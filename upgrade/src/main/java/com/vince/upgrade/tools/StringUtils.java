package com.vince.upgrade.tools;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by by tianweixin on 2018/11/1.
 */
public class StringUtils {
    public static String transferParams(JSONObject originObj) {
        TreeMap<String, String> treeMap = new TreeMap<>();

        Iterator<String> itor = originObj.keys();
        while (itor.hasNext()) {
            String key = itor.next();
            Object obj = originObj.opt(key);
            if ((obj instanceof JSONObject) || (obj instanceof JSONArray)) {
                continue;
            }
            String value = originObj.optString(key);
            if (!originObj.isNull(key) && !TextUtils.isEmpty(value)) {
                treeMap.put(key, value);
            }
        }

        StringBuilder sb = new StringBuilder();

        Iterator<Map.Entry<String, String>> it = treeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            sb.append(entry.getKey() + "=").append(entry.getValue()).append("&");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    //处理传给h5的字符串中转义字符
    public static String parse2JsString(String string) {

        StringBuilder sb = new StringBuilder();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '\'':
                    sb.append("\\\'");
                    break;

                case '\"':
                    sb.append("\\\"");
                    break;

                case '\\':
                    sb.append("\\\\");
                    break;

                case '\b':
                    sb.append("\\b");
                    break;//退格

                case '\f':
                    sb.append("\\f");
                    break;//走纸换页

                case '\n':
                    sb.append("\\n");
                    break;//换行

                case '\r':
                    sb.append("\\r");
                    break;//回车

                case '\t':
                    sb.append("\\t");
                    break;//横向跳格

                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 手机号掩码，中间4位
     *
     * @return
     */
    public static String maskMobileNumber(String mobile) {
        if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
            return mobile;
        }
        String mask = mobile.substring(0, 3) + "****" + mobile.substring(7);

        return mask;
    }

}
