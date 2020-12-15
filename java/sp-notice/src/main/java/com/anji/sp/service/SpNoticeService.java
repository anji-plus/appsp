package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpNoticeForAppVO;
import com.anji.sp.model.vo.SpNoticeVO;

import java.util.List;

/**
 * 公告管理
 *
 * @author kean
 * @date 2020/06/24
 */
public interface SpNoticeService {
    /**
     * 分页根据应用id分页查询公告信息
     *
     * @param reqData
     * @return
     */
    ResponseModel getAppNoticeByAppId(SpNoticeVO reqData);

    /**
     * 新增公告信息
     *
     * @param reqData
     * @return
     */
    ResponseModel insertNotice(SpNoticeVO reqData);

    /**
     * 编辑公告信息
     *
     * @param reqData
     * @return
     */
    ResponseModel updateNotice(SpNoticeVO reqData);
    /**
     * 删除公告信息
     *
     * @param reqData
     * @return
     */
    ResponseModel deleteNotice(SpNoticeVO reqData);

    /**
     * 根据id启用/禁用公告信息
     *
     * @param reqData
     * @return
     */
    ResponseModel enableNotice(SpNoticeVO reqData);

    /**
     * 获取公告信息
     * @param spNoticeVO
     * @return
     */
    List<SpNoticeForAppVO> getNotices(SpNoticeVO spNoticeVO);

    /**
     * 禁用无效公告
     */
    void taskDisableInvalidNotice();
}
