package com.anji.appsp.sdk.version.service;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 版本管理相关功能，目前只有获取版本信息
 * 若有其他功能，添加即可
 * </p>
 */
public interface IAppSpVersionService {
    //设备初始化
    void initDevice();
    //获取版本信息
    void getVersion();
}
