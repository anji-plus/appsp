package com.anji.appsp.sdktest;

import android.app.Application;
import android.text.TextUtils;

import com.anji.appsp.sdk.AppSpConfig;
import com.anji.appsp.sdk.BuildConfig;

public class AppContext extends Application {
    private static AppContext mInstance;
    //appkey是在Appsp创建应用生成的
    public static final String appKey = "aadcfae6215a4e0f9bf5bc5edccb1045";

    public static AppContext getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppSpConfig.getInstance()
                .init(this, appKey)
                //可修改基础请求地址
                .setHost("http://open-appsp.anji-plus.com")
                //正式环境可以禁止日志输出，通过Tag APP-SP过滤看日志
                .setDebuggable(BuildConfig.DEBUG ? true : false)
                //务必要初始化，否则后面请求会报错
                .deviceInit();
    }

}
