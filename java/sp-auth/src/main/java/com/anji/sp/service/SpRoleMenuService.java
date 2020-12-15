package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpRoleMenuPO;
import com.anji.sp.model.vo.SpRoleMenuEditVO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 字典表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpRoleMenuService {
    /**
     * 根据角色role_id 查询对应菜单
     * @param spRoleMenuPO
     * @return
     */
    ResponseModel selectMenuListByRoleId(SpRoleMenuPO spRoleMenuPO);

    /**
     * 更新角色菜单关联表信息
     *
     * @param spRoleMenuEditVO
     * @return
     */
    ResponseModel updateMenuToRole(SpRoleMenuEditVO spRoleMenuEditVO);

    /**
     * 根据角色id删除菜单角色关联关系
     * @param spRoleMenuPO
     * @return
     */
    ResponseModel deleteMenuRoleByRoleId(@RequestBody SpRoleMenuPO spRoleMenuPO);
}