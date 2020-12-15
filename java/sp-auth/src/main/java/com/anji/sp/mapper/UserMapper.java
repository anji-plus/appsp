package com.anji.sp.mapper;

import com.anji.sp.model.po.SpUserPO;
import com.anji.sp.model.vo.SpApplicationVO;
import com.anji.sp.model.vo.SpUserAppInfoVO;
import com.anji.sp.model.vo.SpUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Created by raodeming on 2020/6/22.
 */
public interface UserMapper extends BaseMapper<SpUserPO> {
    List<SpUserVO> selectNoJoinApplicationByAppId(SpApplicationVO spApplicationVO);

    List<SpUserAppInfoVO> selectUserAppRoleByAppId(Long appId);
}
