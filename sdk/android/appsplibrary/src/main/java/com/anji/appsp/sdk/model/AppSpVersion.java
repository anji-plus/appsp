package com.anji.appsp.sdk.model;


import java.io.Serializable;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 版本更新请求返回的内容
 * </p>
 */
public class AppSpVersion implements Serializable {
    /**
     * APK下载地址，包括H5跳转地址
     */
    private String downloadUrl;
    /**
     * 更新日志
     */
    private String updateLog;
    /**
     * 是否显示更新提示
     */
    public boolean showUpdate;
    /**
     * 是否强更
     */
    public boolean mustUpdate;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public boolean isShowUpdate() {
        return showUpdate;
    }

    public void setShowUpdate(boolean showUpdate) {
        this.showUpdate = showUpdate;
    }

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

}