package com.anji.appsp.sdk.notice;

import android.text.TextUtils;

import com.anji.appsp.sdk.AppSpLog;
import com.anji.appsp.sdk.base.AppSpBaseHandler;
import com.anji.appsp.sdk.model.AppSpModel;
import com.anji.appsp.sdk.model.AppSpNoticeModelItem;
import com.anji.appsp.sdk.notice.service.IAppSpNoticeCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 解析获取公告接口返回的json串
 * </p>
 */
public class AppSpNoticeHandler extends AppSpBaseHandler {
    //如果有多个公告功能，可以提供多个callback
    private IAppSpNoticeCallback appSpNoticeCallback;

    public void setAppSpNoticeCallback(IAppSpNoticeCallback appSpNoticeCallback) {
        this.appSpNoticeCallback = appSpNoticeCallback;
    }

    /**
     * 公告获取数据成功
     */
    public void handleNoticeSuccess(String data) {
        if (appSpNoticeCallback != null) {
            synchronized (appSpNoticeCallback) {
                //Notice
                try {
                    AppSpModel<List<AppSpNoticeModelItem>> spVersionModel = new AppSpModel<>();
                    JSONObject jsonObject = new JSONObject(data);
                    spVersionModel.setRepCode(getStringElement(jsonObject.opt("repCode")));
                    spVersionModel.setRepMsg(getStringElement(jsonObject.opt("repMsg")));
                    Object repDtaObj = jsonObject.opt("repData");
                    if (repDtaObj != null && !TextUtils.isEmpty(repDtaObj.toString())
                            && !"null".equalsIgnoreCase(repDtaObj.toString())) {
                        JSONArray repData = new JSONArray(repDtaObj.toString());
                        List<AppSpNoticeModelItem> items = new ArrayList<>();
                        if (repData != null) {
                            for (int i = 0; i < repData.length(); i++) {
                                AppSpNoticeModelItem item = new AppSpNoticeModelItem();
                                JSONObject value = repData.getJSONObject(i);
                                item.setTitle(getStringElement(value.opt("title")));
                                item.setDetails(getStringElement(value.opt("details")));
                                item.setTemplateType(getStringElement(value.opt("templateType")));
                                item.setTemplateTypeName(getStringElement(value.opt("templateTypeName")));
                                items.add(item);
                            }
                            spVersionModel.setRepData(items);
                            appSpNoticeCallback.notice(spVersionModel);
                        }
                    }
                    AppSpLog.d("通知返回客户端数据 " + spVersionModel);
                } catch (Exception e) {
                    AppSpLog.d("通知返回客户端数据 Exception e " + e.toString());
                }

            }
        }
    }

    /**
     * 公告异常处理
     *
     * @param code
     * @param msg
     */
    public void handleNoticeExcption(String code, String msg) {
        if (appSpNoticeCallback != null) {
            synchronized (appSpNoticeCallback) {
                appSpNoticeCallback.error(code, msg);
            }
        }
    }

}
