package com.anji.appsp.sdk.notice.controller;

import android.content.Context;

import com.anji.appsp.sdk.base.AppSpBaseController;
import com.anji.appsp.sdk.notice.AppSpNoticeHandler;
import com.anji.appsp.sdk.notice.impl.AppSpNoticeServiceImpl;
import com.anji.appsp.sdk.notice.service.IAppSpNoticeCallback;
import com.anji.appsp.sdk.notice.service.IAppSpNoticeService;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 公告相关对外提供的方法
 * </p>
 */
public class AppSpNoticeController extends AppSpBaseController {
    public AppSpNoticeController(Context mContext, String appKey) {
        super(mContext, appKey);
    }

    /**
     * 公告回调
     *
     * @param iAppSpNoticeCallback 结果回调接口
     */
    public void getNotice(IAppSpNoticeCallback iAppSpNoticeCallback) {
        AppSpNoticeHandler appSpNoticeHandler = new AppSpNoticeHandler();
        appSpNoticeHandler.setAppSpNoticeCallback(iAppSpNoticeCallback);
        IAppSpNoticeService appspVersionService = new AppSpNoticeServiceImpl(getContext(), getAppKey(), appSpNoticeHandler);
        appspVersionService.getNotice();
    }
}
