package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpVersionForAPPVO;
import com.anji.sp.model.vo.SpVersionVO;

/**
 * 字典表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpVersionService {
    /**
     * 根据平台名称及应用id查询所有版本信息
     *
     * @param reqData
     * @return
     */
    ResponseModel getAppVersionByPlatformAndAppId(SpVersionVO reqData);

    /**
     * 版本新增
     *
     * @param reqData
     * @return
     */
    ResponseModel insertVersion(SpVersionVO reqData);

    /**
     * 更新版本
     *
     * @param reqData
     * @return
     */
    ResponseModel updateVersion(SpVersionVO reqData);
    /**
     * 更新版本信息 不做校验
     *
     * @param reqData
     * @return
     */
    int updateAppVersion(SpVersionVO reqData);

    /**
     * 删除版本
     *
     * @param reqData
     * @return
     */
    ResponseModel deleteVersion(SpVersionVO reqData);

    /**
     * 启用或禁用版本
     *
     * @param reqData
     * @return
     */
    ResponseModel enableVersion(SpVersionVO reqData);

    /**
     * 查询有效的APP version
     *
     * @param spVersionVO
     * @return
     */
    SpVersionForAPPVO getEffectiveVersion(SpVersionVO spVersionVO);

    /**
     * 获取有效版本VO
     *
     * @param spVersionVO
     * @return
     */
    SpVersionVO getEffectiveVersionVO(SpVersionVO spVersionVO);

    /**
     * 更新版本时间
     * @param vo
     * @return
     */
    int updateSpVersionTime(SpVersionVO vo);

    /**
     * 删除无效apk文件
     */
    void deleteInvalidFile();
}