package com.vince.upgrade.tools;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tianweixin on 2015/12/31.
 */
public class SystemRoomUtils {
    public static final String MIUI_PROPERTY_TAG = "ro.miui.ui.version.name";
    public static final String PROP_FOR_FLYME = "ro.build.display.id";

    private static final int UNDEFINE = 0;
    private static final int MIUI = 1;
    private static final int FLYME = 2;

    private static int room = -1;

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {

                }
            }
        }
        return line;
    }

    private static void getSystemRoom() {
        String id = getSystemProperty(PROP_FOR_FLYME);
        if (!TextUtils.isEmpty(id) && id.toLowerCase().contains("flyme")) {
            room = FLYME;
        } else if (!TextUtils.isEmpty(getSystemProperty(MIUI_PROPERTY_TAG))) {
            room = MIUI;
        } else {
            room = UNDEFINE;
        }
    }

    public static boolean isMIUI() {
        if (room == -1) {
            getSystemRoom();
        }
        return room == MIUI;
    }

    public static boolean isFlyme() {
        if (room == -1) {
            getSystemRoom();
        }
        return room == FLYME;
    }

    /**
     * get miui version through ro.miui.ui.version.name
     *
     * @return value of miui version name
     * null, if not miui
     */
    public static float getMiuiVersion() {
        if (isMIUI()) {
            String version = getSystemProperty(MIUI_PROPERTY_TAG);
            if (!TextUtils.isEmpty(version)) {
                try {
                    version = removeNotDigital(version);
                    return Float.valueOf(version);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    private static String removeNotDigital(String text) {
        return text.replaceAll("[^.\\d]", "");
    }

}
