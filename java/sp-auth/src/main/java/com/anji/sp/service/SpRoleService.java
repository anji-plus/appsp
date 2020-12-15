package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpRoleMenuInsertVO;
import com.anji.sp.model.vo.SpRoleVO;

/**
 * 字典表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpRoleService {
    /**
     * 查询角色列表
     *
     * @return
     */
    ResponseModel selectRoleList();

    /**
     * 根据角色id进行删除角色  逻辑删除
     *
     * @param spRoleVO
     * @return
     */
    ResponseModel deleteRoleByRoleId(SpRoleVO spRoleVO);
    /**
     * 根据角色id更新角色
     *
     * @param spRoleVO
     * @return
     */
    ResponseModel updateRoleByRoleId(SpRoleVO spRoleVO);
    /**
     * 插入角色并给用户添加菜单
     *
     * @param spRoleMenuInsertVO
     * @return
     */
    ResponseModel insertRoleAndMenu(SpRoleMenuInsertVO spRoleMenuInsertVO);
}