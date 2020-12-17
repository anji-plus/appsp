package com.anji.appsp.sdk.http;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 接口地址
 * </p>
 */

public class AppSpRequestUrl {
    //基础地址，可改变，便于在开发/测试时配置，如果时生产，建议改成final，不允许改变
    public static String Host = "https://appsp.anji-plus.com/sp/";
    public static final String initDevice = "phone/deviceInit";
    public static final String getAppVersion = "phone/appVersion";
    public static final String getAppNotice = "phone/appNotice";
}
