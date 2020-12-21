package com.anji.appsp.sdk.version.controller;

import android.content.Context;

import com.anji.appsp.sdk.base.AppSpBaseController;
import com.anji.appsp.sdk.version.service.IAppSpVersionCallback;
import com.anji.appsp.sdk.version.AppSpVersionHandler;
import com.anji.appsp.sdk.version.impl.AppSpVersionServiceImpl;
import com.anji.appsp.sdk.version.service.IAppSpVersionService;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 版本相关对外提供的方法，getVersion是目前最主要的
 * </p>
 */
public class AppSpVersionController extends AppSpBaseController {
    public AppSpVersionController(Context mContext, String appKey) {
        super(mContext, appKey);
    }

    /**
     * 初始化不算是业务的一种，且放到版本更新的主功能模块里
     */
    public void initDevice() {
        IAppSpVersionService appspVersionService = new AppSpVersionServiceImpl(getContext(), getAppKey());
        appspVersionService.initDevice();
    }

    /**
     * 版本更新回调
     *
     * @param iAppSpVersionCallback 结果回调接口
     */
    public void getVersion(IAppSpVersionCallback iAppSpVersionCallback) {
        AppSpVersionHandler appSpVersionHandler = new AppSpVersionHandler();
        appSpVersionHandler.setIAppSpVersionUpdateCallback(iAppSpVersionCallback);
        IAppSpVersionService appspVersionService = new AppSpVersionServiceImpl(getContext(), getAppKey(), appSpVersionHandler);
        appspVersionService.getVersion();
    }

}
