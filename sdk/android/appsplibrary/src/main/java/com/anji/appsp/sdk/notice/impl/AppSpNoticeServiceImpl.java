package com.anji.appsp.sdk.notice.impl;

import android.content.Context;
import android.text.TextUtils;

import com.anji.appsp.sdk.AppSpLog;
import com.anji.appsp.sdk.base.AppSpBaseRequest;
import com.anji.appsp.sdk.http.AppSpCallBack;
import com.anji.appsp.sdk.http.AppSpHttpClient;
import com.anji.appsp.sdk.http.AppSpPostData;
import com.anji.appsp.sdk.http.AppSpRequestUrl;
import com.anji.appsp.sdk.notice.AppSpNoticeHandler;
import com.anji.appsp.sdk.notice.service.IAppSpNoticeService;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 获取公告接口
 * </p>
 */
public class AppSpNoticeServiceImpl extends AppSpBaseRequest implements IAppSpNoticeService {
    private AppSpNoticeHandler appSpNoticeHandler;

    public AppSpNoticeServiceImpl(Context mContext, String appKey, AppSpNoticeHandler appSpNoticeHandler) {
        super(mContext, appKey);
        this.appSpNoticeHandler = appSpNoticeHandler;
    }

    @Override
    public void getNotice() {
        if (TextUtils.isEmpty(appKey)) {
            AppSpLog.e("getNotice Appkey is null or empty");
            return;
        }
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.getAppNotice, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
                if (appSpNoticeHandler != null) {
                    appSpNoticeHandler.handleNoticeSuccess(data);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (appSpNoticeHandler != null) {
                    appSpNoticeHandler.handleNoticeExcption(code, msg);
                }
            }
        });
    }
}
