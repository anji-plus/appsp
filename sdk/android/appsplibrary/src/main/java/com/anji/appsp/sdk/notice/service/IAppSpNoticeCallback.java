package com.anji.appsp.sdk.notice.service;

import com.anji.appsp.sdk.model.AppSpModel;
import com.anji.appsp.sdk.model.AppSpNoticeModelItem;

import java.util.List;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 获取公告回调接口
 * 对外的接口，方便集成
 * </p>
 */
public interface IAppSpNoticeCallback {
    /**
     * @param appSpModel 公告详情，支持多个公告，公告类型暂且支持对话框和跑马灯
     */
    void notice(AppSpModel<List<AppSpNoticeModelItem>> appSpModel);

    /**
     * 请求错误，未能获取后端数据
     * @param code 状态码
     * @param msg  错误信息
     */
    void error(String code,String msg);
}
