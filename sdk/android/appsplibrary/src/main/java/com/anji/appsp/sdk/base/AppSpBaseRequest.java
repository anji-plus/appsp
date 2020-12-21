package com.anji.appsp.sdk.base;

import android.content.Context;
import android.os.Build;

import com.anji.appsp.sdk.AppSpLog;
import com.anji.appsp.sdk.http.AppSpPostData;
import com.anji.appsp.sdk.util.PhoneUtil;
import com.anji.appsp.sdk.util.RSAUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * Appsp 提供公共基础数据
 * </p>
 */
public class AppSpBaseRequest {
    protected static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDf2XdSpvtuC1xeRzP8VOhoF4CRrfOk0ZbxlYniauPWmQqNeDP1IMqoXyJ72FEmmFh6crNSKdGwqyy9ifRuiCPbQ0UhKMsOe7F/06FECoUH3NYipVIHgY4KYpHrIYo9uw8xTCWjUh9iz3Kv5yhulzHR+ORpNQ460xTki57yd6LA/wIDAQAB";
    protected Context context;
    protected String appKey;

    public AppSpBaseRequest(Context context, String appKey) {
        this.context = context;
        this.appKey = appKey;
    }

    /**
     * 获取设备基本信息
     * 这个在未来做更新策略或者数据统计时有帮助
     *
     * @return
     * @throws JSONException
     */
    private JSONObject getRequestModelJsonObj() throws JSONException {
        String deviceId = PhoneUtil.getDeviceId(context);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appKey", appKey)
                .put("platform", "Android")
                .put("brand", Build.MODEL)
                .put("osVersion", Build.VERSION.RELEASE)
                .put("sdkVersion", Build.VERSION.SDK_INT)
                .put("deviceId", deviceId)
                .put("screenInfo", PhoneUtil.getScreenInfo(context))
                .put("versionCode", PhoneUtil.getVersionCode(context))
                .put("versionName", PhoneUtil.getVersionName(context))
                .put("netWorkStatus", PhoneUtil.getNetworkState(context));
        return jsonObject;
    }

    /**
     * 获取公共的请求数据
     *
     * @return
     * @hide
     */
    protected AppSpPostData getPostEncryptData() {
        AppSpPostData appSpPostData = new AppSpPostData();
        JSONObject jsonObject = null;
        try {
            jsonObject = getRequestModelJsonObj();
        } catch (Exception e) {

        }
        if (jsonObject != null) {
            String jsonStr = jsonObject.toString().replaceAll("\"", "\"");
            AppSpLog.d("getPostEncryptData json " + jsonStr);
            appSpPostData.put("data", jsonStr);
        }
        //加密处理
        String signStr = "";
        PublicKey publicKey;
        try {
            publicKey = RSAUtil.getPublicKey(this.publicKey);
            signStr = RSAUtil.encrypt(jsonObject.toString(), publicKey);

        } catch (Exception e) {

        }
        AppSpLog.d("signStr " + signStr);
        appSpPostData.put("sign", signStr);
        return appSpPostData;
    }
}
