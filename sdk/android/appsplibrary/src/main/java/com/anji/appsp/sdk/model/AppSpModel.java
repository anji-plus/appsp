package com.anji.appsp.sdk.model;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 后端返回的结果
 * </p>
 */
public class AppSpModel<T> {
    private String repCode;
    private String repMsg;
    private T repData;

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getRepMsg() {
        return repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    public T getRepData() {
        return repData;
    }

    public void setRepData(T repData) {
        this.repData = repData;
    }

    @Override
    public String toString() {
        return "AppSpModel{" +
                "repCode='" + repCode + '\'' +
                ", repMsg='" + repMsg + '\'' +
                ", repData=" + repData +
                '}';
    }
}
