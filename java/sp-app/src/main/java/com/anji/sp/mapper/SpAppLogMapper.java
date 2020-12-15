package com.anji.sp.mapper;

import com.anji.sp.model.po.SpAppLogPO;
import com.anji.sp.model.vo.SpAppLogVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * app请求log表
 *
 * @author Kean 2020-06-23
 */
public interface SpAppLogMapper extends BaseMapper<SpAppLogPO> {
    Long getDeviceIdCount(SpAppLogVO spAppLogVO);
}