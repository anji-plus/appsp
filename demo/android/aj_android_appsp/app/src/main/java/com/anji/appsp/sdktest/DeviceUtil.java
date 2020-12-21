package com.anji.appsp.sdktest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;


public class DeviceUtil {
    protected static String sDeviceId;
    static int screen_width;
    static int screen_height;
    static int screen_density_dpi;
    static int stateBar_height;
    private static String packageName;

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("com.jinjiajinrong.zq", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.jinjiajinrong.zq", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static int getScreenWidth() {
        if (screen_width == 0) {
            initDeviceInfo();
        }

        return screen_width;
    }

    public static int getScreenHeight() {
        if (screen_height == 0) {
            initDeviceInfo();
        }

        return screen_height;
    }

    public static int getStateBarHeight() {
        if (stateBar_height == 0) {
            int resourceId = Resources.getSystem().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                stateBar_height = Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        }
        return stateBar_height;
    }


    public static int getScreenDpi() {
        if (screen_density_dpi == 0) {
            initDeviceInfo();
        }

        return screen_density_dpi;
    }

    static void initDeviceInfo() {
        DisplayMetrics displayMetrics = AppContext.getInstance().getResources().getDisplayMetrics();
        screen_width = displayMetrics.widthPixels;
        screen_height = displayMetrics.heightPixels;
        screen_density_dpi = displayMetrics.densityDpi;
    }
}