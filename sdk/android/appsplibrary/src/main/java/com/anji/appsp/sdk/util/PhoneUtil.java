package com.anji.appsp.sdk.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.anji.appsp.sdk.AppSpLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 获取设备信息
 * </p>
 */
public class PhoneUtil {
    public static String getDeviceId(Context context) {
        return getDeviceId(context, 128);
    }

    /**
     * Android_id + Wifi-MAC
     * 最多获取128位
     *
     * @param context
     * @param length  最大位数
     * @return
     */
    public static String getDeviceId(Context context, int length) {
        String androidId = getAndroidId(context);
        String mac = getMac(context);
        AppSpLog.d(" getDeviceId androidId " + androidId + " mac " + mac);
        String deviceId = androidId + mac;
        //截取前128位
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getPhoneUUID();
        } else if (deviceId.length() >= length) {
            deviceId = deviceId.substring(0, length);
        }
        return deviceId;
    }

    @SuppressLint("MissingPermission")
    public static String[] getNetworkAccessMode(Context paramContext) {
        String[] arrayOfString = {"", ""};
        if (paramContext == null) {
            return arrayOfString;
        }
        try {
            if (!checkPermission(paramContext, Manifest.permission.ACCESS_NETWORK_STATE)) {
                arrayOfString[0] = "";
                return arrayOfString;
            }
            ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (localConnectivityManager == null) {
                arrayOfString[0] = "";
                return arrayOfString;
            }
            NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(1);
            if ((localNetworkInfo1 != null) &&
                    (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED)) {
                arrayOfString[0] = "Wi-Fi";
                return arrayOfString;
            }
            NetworkInfo localNetworkInfo2 = localConnectivityManager.getNetworkInfo(0);
            if ((localNetworkInfo2 != null) &&
                    (localNetworkInfo2.getState() == NetworkInfo.State.CONNECTED)) {
                arrayOfString[0] = "2G/3G";
                arrayOfString[1] = localNetworkInfo2.getSubtypeName();
                return arrayOfString;
            }
        } catch (Throwable localThrowable) {
        }
        return arrayOfString;
    }

    /**
     * 获取WIFI Mac地址
     *
     * @param paramContext
     * @return
     */
    public static String getMac(Context paramContext) {
        String str = "";
        if (paramContext == null) {
            return str;
        }
        if (Build.VERSION.SDK_INT < 23) {
            str = getMacBySystemInterface(paramContext);
        } else if (Build.VERSION.SDK_INT == 23) {
            str = getMacByJavaAPI();
            if (TextUtils.isEmpty(str)) {
                str = getMacShell();
            }
        } else {
            str = getMacByJavaAPI();
            if (TextUtils.isEmpty(str)) {
                str = getMacBySystemInterface(paramContext);
            }
        }
        return str;
    }

    @SuppressLint("MissingPermission")
    private static String getMacBySystemInterface(Context paramContext) {
        if (paramContext == null) {
            return "";
        }
        try {
            WifiManager localWifiManager = (WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(paramContext, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
                return localWifiInfo.getMacAddress();
            }
            return "";
        } catch (Throwable localThrowable) {
        }
        return "";
    }


    private static String getMacByJavaAPI() {
        try {
            Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration.nextElement();
                if (("wlan0".equals(localNetworkInterface.getName())) || ("eth0".equals(localNetworkInterface.getName()))) {
                    byte[] arrayOfByte1 = localNetworkInterface.getHardwareAddress();
                    if ((arrayOfByte1 == null) || (arrayOfByte1.length == 0)) {
                        return "";
                    }
                    StringBuilder localStringBuilder = new StringBuilder();
                    for (byte b : arrayOfByte1) {
                        localStringBuilder.append(String.format("%02X:", new Object[]{Byte.valueOf(b)}));
                    }
                    if (localStringBuilder.length() > 0) {
                        localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
                    }
                    return localStringBuilder.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable localThrowable) {
        }
        return "";
    }

    private static String getMacShell() {
        try {
            String[] arrayOfString = {"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/devices/virtual/net/wlan0/address"};
            for (int i = 0; i < arrayOfString.length; i++) {
                try {
                    String str = reaMac(arrayOfString[i]);
                    if (str != null) {
                        return str;
                    }
                } catch (Throwable localThrowable2) {
                }
            }
        } catch (Throwable localThrowable1) {
        }
        return "";
    }

    private static String reaMac(String paramString) {
        String str = null;
        try {
            FileReader localFileReader = new FileReader(paramString);
            BufferedReader localBufferedReader = null;
            if (localFileReader != null) {
                try {
                    localBufferedReader = new BufferedReader(localFileReader, 1024);
                    str = localBufferedReader.readLine();
                } finally {
                    if (localFileReader != null) {
                        try {
                            localFileReader.close();
                        } catch (Throwable localThrowable4) {
                        }
                    }
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (Throwable localThrowable5) {
                        }
                    }
                }
            }
            return str;
        } catch (Throwable localThrowable1) {
        }
        return str;
    }

    /**
     * 获取IMEI
     *
     * @param paramContext
     * @return
     */
    @SuppressLint({"MissingPermission"})
    public static String getImei(Context paramContext) {
        String str = "";
        try {
            if (paramContext != null) {
                TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE);
                if ((localTelephonyManager != null) &&
                        (checkPermission(paramContext, Manifest.permission.READ_PHONE_STATE))) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        try {
                            Method localMethod = localTelephonyManager.getClass().getMethod("getImei", new Class[0]);
                            localMethod.setAccessible(true);
                            str = (String) localMethod.invoke(localTelephonyManager, new Object[0]);
                        } catch (Exception localException2) {
                        }
                        if (TextUtils.isEmpty(str)) {
                            str = localTelephonyManager.getDeviceId();
                        }
                    } else {
                        str = localTelephonyManager.getDeviceId();
                    }
                }
            }
        } catch (Exception localException1) {
        }
        return str;
    }

    /**
     * 获取IMSI
     *
     * @param paramContext
     * @return
     */
    @SuppressLint({"MissingPermission"})
    public static String getImsi(Context paramContext) {
        if (paramContext == null) {
            return null;
        }
        TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE);
        String str = null;
        if (checkPermission(paramContext, Manifest.permission.READ_PHONE_STATE)) {
            str = localTelephonyManager.getSubscriberId();
        }
        return str;
    }

    /**
     * 获取MEID
     *
     * @param paramContext
     * @return
     */
    @SuppressLint({"MissingPermission"})
    public static String getMeid(Context paramContext) {
        if (paramContext == null) {
            return "";
        }
        String str = "";
        TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE);
        if ((checkPermission(paramContext, Manifest.permission.READ_PHONE_STATE)) &&
                (localTelephonyManager != null)) {
            if (Build.VERSION.SDK_INT < 26) {
                str = localTelephonyManager.getDeviceId();
            } else {
                try {
                    str = getMeidReal(paramContext);
                    if (TextUtils.isEmpty(str)) {
                        str = localTelephonyManager.getDeviceId();
                    }
                } catch (Throwable localThrowable) {
                }
            }
        }
        return str;
    }

    private static String getMeidReal(Context paramContext) {
        if (paramContext == null) {
            return "";
        }
        String str = "";
        try {
            Class localClass = Class.forName("android.telephony.TelephonyManager");
            Method localMethod = localClass.getMethod("getMeid", new Class[0]);
            Object localObject = localMethod.invoke(null, new Object[0]);
            if ((null != localObject) && ((localObject instanceof String))) {
                str = (String) localObject;
            }
        } catch (Exception localException) {
        }
        return str;
    }

    private static String getNotNullStr(Object input) {
        return input == null ? "" : input.toString();
    }


    /**
     * android id,64位
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return getNotNullStr(androidId);
    }

    /**
     * 自动生成一个IMEI号码
     *
     * @return
     */
    private static String getPhoneUUID() {
        StringBuffer sb = new StringBuffer();
        sb.append("35"); //+ //we make this look like a valid IMEI
        sb.append(getNotNullStr(Build.BOARD.length() % 10));
        sb.append(getNotNullStr(Build.BRAND.length() % 10));
        sb.append(getNotNullStr(Build.CPU_ABI.length() % 10));
        sb.append(getNotNullStr(Build.DEVICE.length() % 10));
        sb.append(getNotNullStr(Build.DISPLAY.length() % 10));
        sb.append(getNotNullStr(Build.HOST.length() % 10));
        sb.append(getNotNullStr(Build.ID.length() % 10));
        sb.append(getNotNullStr(Build.MANUFACTURER.length() % 10));
        sb.append(getNotNullStr(Build.MODEL.length() % 10));
        sb.append(getNotNullStr(Build.PRODUCT.length() % 10));
        sb.append(getNotNullStr(Build.TAGS.length() % 10));
        sb.append(getNotNullStr(Build.TYPE.length() % 10));
        sb.append(getNotNullStr(Build.USER.length() % 10)); //13 digits
        String imei = sb.toString();
        return imei;
    }

    /**
     * 获取屏幕信息
     *
     * @param context
     * @return
     */
    public static String getScreenInfo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels + "*" + displayMetrics.widthPixels;
    }

    /**
     * 获取网络方式 4G/3G/2G/WIFI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getNetworkState(Context context) {
        String strNetworkType = "unknown";
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return strNetworkType;
        }
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * @return 当前APK版本号
     */
    public static long getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            //如果SDK>=28，返回long型的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return info.getLongVersionCode();
            }
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * @return 当前APK版本名
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 权限校验
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean checked = false;
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class localClass = Class.forName("android.content.Context");
                Method method = localClass.getMethod("checkSelfPermission", new Class[]{String.class});
                int result = ((Integer) method.invoke(context, new Object[]{permission})).intValue();
                if (result == PackageManager.PERMISSION_GRANTED) {
                    checked = true;
                } else {
                    checked = false;
                }
            } catch (Exception localException) {
                checked = false;
            }
        } else {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                checked = true;
            }
        }
        return checked;
    }
}
