package com.anji.sp.mapper;

import com.anji.sp.model.po.SpRoleMenuPO;
import com.anji.sp.model.vo.SpRoleMenuVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 角色
 *
 * @author Kean 2020-06-23
 */
public interface SpRoleMenuMapper extends BaseMapper<SpRoleMenuPO> {
    /**
     * @param roleId
     * @return
     */
    List<SpRoleMenuVO> selectMenuListByRoleId(Long roleId);
}