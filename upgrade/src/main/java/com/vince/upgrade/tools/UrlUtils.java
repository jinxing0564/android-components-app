package com.vince.upgrade.tools;

import android.net.Uri;

/**
 * Created by by tianweixin on 2018/10/26.
 */
public class UrlUtils {
    public static final String SCHEME_AIWUYU = "aiwuyu";

    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";

    public static boolean isHttpUrl(String url) {
        String scheme = Uri.parse(url).getScheme();
        return SCHEME_HTTP.equalsIgnoreCase(scheme) || SCHEME_HTTPS.equalsIgnoreCase(scheme);
    }

    public static boolean isAiwuyuUrl(String url) {
        String scheme = Uri.parse(url).getScheme();
        return SCHEME_AIWUYU.equalsIgnoreCase(scheme);
    }
}
