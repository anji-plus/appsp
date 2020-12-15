package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpApplicationPO;
import com.anji.sp.model.vo.SpApplicationVO;

import java.util.List;

/**
 * 应用表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpApplicationService {
    /**
     * 查询所有应用
     *
     * @return
     */
    ResponseModel queryApplicationList();

    /**
     * 查询应用列表
     * @return
     */
    List<SpApplicationPO> selectApplicationList();

    /**
     * 分页查询应用
     *
     * @param reqData
     * @return
     */
    ResponseModel queryApplicationByPage(SpApplicationVO reqData);

    /**
     * 新建应用
     *
     * @param reqData
     * @return
     */
    ResponseModel insertApplication(SpApplicationVO reqData);

    /**
     * 删除应用
     *
     * @param reqData
     * @return
     */
    ResponseModel deleteApplication(SpApplicationVO reqData);

    /**
     * 更新应用名及app_key
     *
     * @param reqData
     * @return
     */
    ResponseModel updateApplication(SpApplicationVO reqData);

    /**
     * 根据appId查询应用信息
     *
     * @param appId
     * @return
     */
    SpApplicationPO selectByAppId(Long appId);

    /**
     * 根据appKey查询应用信息
     *
     * @param appKey
     * @return
     */
    SpApplicationPO selectByAppKey(String appKey);

}