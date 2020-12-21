package com.anji.appsp.sdk.base;

import android.content.Context;

public class AppSpBaseController {
    protected Context context;
    protected String appKey;

    public AppSpBaseController(Context mContext, String appKey) {
        this.context = mContext;
        this.appKey = appKey;
    }

    public Context getContext() {
        return context;
    }

    public String getAppKey() {
        return appKey;
    }
}
