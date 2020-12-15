package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpAppReqDataVO;

import javax.servlet.http.HttpServletRequest;

/**
 * app  接口
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpAppReqService {
    /**
     * 初始化保存用户设备信息
     *
     * @param spAppReqDataVO
     * @param request
     * @return
     */
    ResponseModel deviceInit(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request);

    /**
     * 获取APP 版本 信息
     *
     * @param spAppReqDataVO
     * @param request
     * @return
     */
    ResponseModel getAppVersion(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request);

    /**
     * 获取APP 公告 信息
     *
     * @param spAppReqDataVO
     * @param request
     * @return
     */
    ResponseModel getAppNotice(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request);
}