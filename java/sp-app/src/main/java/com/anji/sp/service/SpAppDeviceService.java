package com.anji.sp.service;

import com.anji.sp.model.po.SpAppLogPO;
import com.anji.sp.model.vo.SpAppLogVO;

/**
 * app  接口
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpAppDeviceService {

    /**
     * 更新device表
     *
     * @param spAppLogPO
     * @return
     */
    int updateDeviceInfo(SpAppLogPO spAppLogPO);

    /**
     * 查询对应appkey的device表
     *
     * @param spAppLogVO
     * @return
     */
    int selectCount(SpAppLogVO spAppLogVO);
}