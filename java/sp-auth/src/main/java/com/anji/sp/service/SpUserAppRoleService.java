package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpUserAppRolePO;
import com.anji.sp.model.vo.SpUserAppInfoVO;
import com.anji.sp.model.vo.SpUserVO;

import java.util.Map;
import java.util.Set;

/**
 * 应用表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpUserAppRoleService {
    /**
     * 插入关联关系
     *
     * @param reqData
     * @return
     */
    ResponseModel insert(SpUserAppRolePO reqData);
    /**
     * 删除用户项目关联表数据
     * 删除用户项目角色关联表数据
     *
     * @param reqData
     * @return
     */
    ResponseModel delete(SpUserAppRolePO reqData);

    /**
     * 更改项目用户角色
     *
     * @param reqData
     * @return
     */
    ResponseModel update(SpUserAppRolePO reqData);

    /**
     * 分页查询关联关系用户信息
     *
     * @param reqData
     * @return
     */
    ResponseModel selectUserAppRoleByAppId(SpUserAppInfoVO reqData);

    /**
     * -- 根据用户ID查询项目信息
     *
     * @return
     */
    ResponseModel selectAppInfoByUserId();

    /**
     * 根据 appId 和 userId 查询菜单信息
     *
     * @param reqData
     * @return
     */
    ResponseModel selectMenuPermissionsByAppIDAndUserId(SpUserAppInfoVO reqData);

    /**
     * 查询用户所有菜单权限
     *
     * @return
     */
    Map<Long, Set<String>> selectUserMenuPerms(SpUserVO spUserVO);


}