package com.anji.sp.mapper;

import com.anji.sp.model.po.SpNoticePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 公告管理
 *
 * @author kean
 * @date 2020/06/24
 */
public interface SpNoticeMapper extends BaseMapper<SpNoticePO> {

    int setNoticeEnableInvalid();
}
