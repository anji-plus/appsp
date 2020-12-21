package com.anji.appsp.sdk.version.impl;

import android.content.Context;
import android.text.TextUtils;

import com.anji.appsp.sdk.AppSpLog;
import com.anji.appsp.sdk.base.AppSpBaseRequest;
import com.anji.appsp.sdk.http.AppSpCallBack;
import com.anji.appsp.sdk.http.AppSpHttpClient;
import com.anji.appsp.sdk.http.AppSpPostData;
import com.anji.appsp.sdk.http.AppSpRequestUrl;
import com.anji.appsp.sdk.version.AppSpVersionHandler;
import com.anji.appsp.sdk.version.service.IAppSpVersionService;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 获取版本接口
 * </p>
 */
public class AppSpVersionServiceImpl extends AppSpBaseRequest implements IAppSpVersionService {
    private AppSpVersionHandler appSpVersionHandler;

    public AppSpVersionServiceImpl(Context mContext, String appKey) {
        super(mContext, appKey);
    }

    public AppSpVersionServiceImpl(Context mContext, String appKey, AppSpVersionHandler appSpVersionHandler) {
        super(mContext, appKey);
        this.appSpVersionHandler = appSpVersionHandler;
    }

    @Override
    public void initDevice() {
        if(TextUtils.isEmpty(appKey)){
            AppSpLog.e("initDevice Appkey is null or empty");
            return;
        }
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.initDevice, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
                AppSpLog.d("initDevice success");
            }

            @Override
            public void onError(String code, String msg) {
                AppSpLog.d("initDevice error");
            }
        });
    }

    @Override
    public void getVersion() {
        if(TextUtils.isEmpty(appKey)){
            AppSpLog.e("getVersion Appkey is null or empty");
            return;
        }
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.getAppVersion, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
                if (appSpVersionHandler != null) {
                    appSpVersionHandler.handleVersionSuccess(data);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (appSpVersionHandler != null) {
                    appSpVersionHandler.handleUpdateException(code, msg);
                }
            }
        });
    }
}
