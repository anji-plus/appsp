package com.anji.sp.mapper;

import com.anji.sp.model.po.SpUserAppRolePO;
import com.anji.sp.model.vo.SpAppInfoVO;
import com.anji.sp.model.vo.SpMenuVO;
import com.anji.sp.model.vo.SpUserAppInfoVO;
import com.anji.sp.model.vo.SpUserMenuVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 公告管理
 *
 * @author kean
 * @date 2020/06/24
 */
public interface SpUserAppRoleMapper extends BaseMapper<SpUserAppRolePO> {

    List<SpAppInfoVO> selectAppInfoByUserId(SpUserAppInfoVO reqData);
    List<SpAppInfoVO> selectAppInfoByAdmin(SpUserAppInfoVO reqData);

    List<SpMenuVO> selectMenuPermissionsByAppIDAndUserId(SpUserAppInfoVO reqData);

    List<SpUserMenuVO> selectUserMenuPerms(SpUserMenuVO reqData);
}
