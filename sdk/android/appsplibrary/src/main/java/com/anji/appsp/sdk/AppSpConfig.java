package com.anji.appsp.sdk;

import android.content.Context;

import com.anji.appsp.sdk.http.AppSpRequestUrl;
import com.anji.appsp.sdk.notice.controller.AppSpNoticeController;
import com.anji.appsp.sdk.notice.service.IAppSpNoticeCallback;
import com.anji.appsp.sdk.version.controller.AppSpVersionController;
import com.anji.appsp.sdk.version.service.IAppSpVersionCallback;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 1，入口类，初始化，获取接口回调
 * 2，单例
 * 3，为了集成的便携性和兼容性，SDK没有引入第三方jar包，
 * 所以可以看到，json的解析，也是采用的原始方式
 * 4，服务端校验数据完整性，加入sign参数，采用RSA加密方式，前端和后端约定公钥
 * </p>
 */
public final class AppSpConfig {
    private static volatile AppSpConfig appSpConfig;
    //版本更新
    private Context context;
    private String appKey;

    private AppSpConfig() {
    }

    public static synchronized AppSpConfig getInstance() {
        if (appSpConfig == null) {
            appSpConfig = new AppSpConfig();
        }
        return appSpConfig;
    }

    /**
     * @param host 请求的基本地址
     */
    public AppSpConfig setHost(String host) {
        AppSpRequestUrl.Host = host;
        return appSpConfig;
    }

    /**
     * 设置是否打开debug开关
     *
     * @param debug true表示打开debug
     */
    public AppSpConfig setDebuggable(boolean debug) {
        AppSpLog.setDebugged(debug);
        return appSpConfig;
    }

    /**
     * 初始化
     *
     * @param context 可以是 activity/fragment/view
     * @param appKey  用于标识哪个APP，唯一标识
     */
    public AppSpConfig init(Context context, String appKey) {
        this.context = context;
        this.appKey = appKey;
        return appSpConfig;
    }

    /**
     * 初始化
     */
    public void deviceInit() {
        AppSpVersionController appSpVersionController = new AppSpVersionController(context, appKey);
        appSpVersionController.initDevice();
    }

    /**
     * 版本更新回调
     *
     * @param iAppSpVersionCallback
     */
    public void getVersion(IAppSpVersionCallback iAppSpVersionCallback) {
        AppSpVersionController appSpVersionController = new AppSpVersionController(context, appKey);
        appSpVersionController.getVersion(iAppSpVersionCallback);
    }

    /**
     * 公告回调
     *
     * @param iAppSpNoticeCallback
     */
    public void getNotice(IAppSpNoticeCallback iAppSpNoticeCallback) {
        AppSpNoticeController appSpNoticeController = new AppSpNoticeController(context, appKey);
        appSpNoticeController.getNotice(iAppSpNoticeCallback);
    }

}
