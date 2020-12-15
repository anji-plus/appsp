package com.anji.sp.controller;

import com.anji.sp.aspect.Log;
import com.anji.sp.aspect.PreSpAuthorize;
import com.anji.sp.enums.BusinessType;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpUserAppRolePO;
import com.anji.sp.model.vo.SpUserAppInfoVO;
import com.anji.sp.service.SpUserAppRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by raodeming on 2020/6/23.
 */
@RestController
@RequestMapping("/userAppRole")
@Api(tags = "用户应用角色关联")
public class SpUserAppRoleController {

    @Autowired
    private SpUserAppRoleService userAppRoleService;

    @ApiOperation(value = "分页查询关联关系用户信息", httpMethod = "POST")
    @PostMapping("/selectByAppId/v1")
    @PreSpAuthorize("system:user:members")
    public ResponseModel selectByAppId(@RequestBody SpUserAppInfoVO reqData) {
        return userAppRoleService.selectUserAppRoleByAppId(reqData);
    }

    @ApiOperation(value = "新增用户应用角色关联", httpMethod = "POST")
    @PostMapping("/insert/v1")
    @PreSpAuthorize("system:user:members")
    @Log(title = "新增应用用户", businessType = BusinessType.INSERT)
    public ResponseModel insert(@RequestBody SpUserAppRolePO userAppRolePO) {
        return userAppRoleService.insert(userAppRolePO);
    }

    @ApiOperation(value = "删除用户项目关联表数据", httpMethod = "POST")
    @PostMapping("/delete/v1")
    @PreSpAuthorize("system:user:members")
    @Log(title = "删除应用用户", businessType = BusinessType.DELETE)
    public ResponseModel delete(@RequestBody SpUserAppRolePO reqData) {
        return userAppRoleService.delete(reqData);
    }

    @ApiOperation(value = "更改项目用户角色", httpMethod = "POST")
    @PostMapping("/update/v1")
    @PreSpAuthorize("system:user:members")
    @Log(title = "更改应用用户角色", businessType = BusinessType.UPDATE)
    public ResponseModel update(@RequestBody SpUserAppRolePO reqData) {
        return userAppRoleService.update(reqData);
    }

    @ApiOperation(value = "根据用户查询项目信息", httpMethod = "POST")
    @PostMapping("/selectAppInfo/v1")
    public ResponseModel selectAppInfo() {
        return userAppRoleService.selectAppInfoByUserId();
    }

    @ApiOperation(value = "根据 appId 和 userId 查询菜单信息", httpMethod = "POST")
    @PostMapping("/selectMenuPermissionsByAppIDAndUserId/v1")
    public ResponseModel selectMenuPermissionsByAppIDAndUserId(@RequestBody SpUserAppInfoVO reqData) {
        return userAppRoleService.selectMenuPermissionsByAppIDAndUserId(reqData);
    }

//    @ApiOperation(value = "查询用户菜单信息",  httpMethod = "POST")
//    @PostMapping("/selectPerms")
//    public ResponseModel selectPerms() {
//        return userAppRoleService.selectPerms();
//    }


}
