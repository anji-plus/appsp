package com.anji.appsp.sdk.http;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 请求结果的回调，成功会把数据全部返回，失败把状态码和错误信息返回
 * </p>
 */
public interface AppSpCallBack {

    /**
     * 请求成功且业务代码正常
     * @param data 返回处理后数据
     */
    void onSuccess(String data);

    /**
     * 在请求失败或者业务代码异常时调用
     * @param code  状态码，分为业务码和网络状态码
     *              业务码分为
     * @param msg   错误日志
     */
    void onError(String code, String msg);
}
