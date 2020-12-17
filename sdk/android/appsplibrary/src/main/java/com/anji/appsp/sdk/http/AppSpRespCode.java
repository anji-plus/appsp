package com.anji.appsp.sdk.http;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 请求返回状态码，0000为正常
 * </p>
 */
public class AppSpRespCode {
    public static final String SUCCESS = "0000";//正常
    public static final String REQUEST_EXCEPTION = "1001";//请求异常
    public static final String OS_VERSION_INVALID = "1207";//系统版本号为空
    public static final String APP_VERSION_INVALID = "1208";//应用版本号为空
    public static final String APPKEY_EMPTY_ERROR = "1202";//appKey为空
    public static final String APPKEY_VERIFT_ERROR = "1203";//appKey校验失败
}
