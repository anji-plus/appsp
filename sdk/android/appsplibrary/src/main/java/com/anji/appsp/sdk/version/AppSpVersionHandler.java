package com.anji.appsp.sdk.version;

import android.text.TextUtils;

import com.anji.appsp.sdk.base.AppSpBaseHandler;
import com.anji.appsp.sdk.AppSpLog;
import com.anji.appsp.sdk.version.service.IAppSpVersionCallback;
import com.anji.appsp.sdk.model.AppSpModel;
import com.anji.appsp.sdk.model.AppSpVersion;

import org.json.JSONObject;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 解析获取版本接口返回的json串
 * </p>
 */
public class AppSpVersionHandler extends AppSpBaseHandler {
    private IAppSpVersionCallback appSpVersionCallback;

    public void setIAppSpVersionUpdateCallback(IAppSpVersionCallback appSpVersionCallback) {
        this.appSpVersionCallback = appSpVersionCallback;
    }

    /**
     * 版本更新获取数据成功
     * json解析
     */
    public void handleVersionSuccess(String data) {
        if (appSpVersionCallback != null) {
            synchronized (appSpVersionCallback) {
                //Update
                try {
                    AppSpModel<AppSpVersion> spVersionModel = new AppSpModel<>();
                    JSONObject jsonObject = new JSONObject(data);
                    spVersionModel.setRepCode(getStringElement(jsonObject.opt("repCode")));
                    spVersionModel.setRepMsg(getStringElement(jsonObject.opt("repMsg")));

                    Object repDtaObj = jsonObject.opt("repData");
                    if (repDtaObj != null && !TextUtils.isEmpty(repDtaObj.toString())
                            && !"null".equalsIgnoreCase(repDtaObj.toString())) {
                        JSONObject repData = new JSONObject(repDtaObj.toString());
                        if (repData != null) {
                            AppSpVersion appSpVersion = new AppSpVersion();
                            appSpVersion.setDownloadUrl(getStringElement(repData.opt("downloadUrl")));
                            appSpVersion.setUpdateLog(getStringElement(repData.opt("updateLog")));
                            appSpVersion.setShowUpdate(getBooleanElement(repData.opt("showUpdate")));
                            appSpVersion.setMustUpdate(getBooleanElement(repData.opt("mustUpdate")));
                            spVersionModel.setRepData(appSpVersion);
                        }
                    }
                    AppSpLog.d("版本更新返回客户端数据 " + spVersionModel);
                    appSpVersionCallback.update(spVersionModel);
                } catch (Exception e) {
                    AppSpLog.d("版本更新返回客户端数据 Exception e " + e.toString());
                }
            }
        }

    }

    /**
     * 版本更新异常处理
     *
     * @param code
     * @param msg
     */
    public void handleUpdateException(String code, String msg) {
        if (appSpVersionCallback != null) {
            synchronized (appSpVersionCallback) {
                //Update error
                appSpVersionCallback.error(code, msg);
            }
        }
    }

}
