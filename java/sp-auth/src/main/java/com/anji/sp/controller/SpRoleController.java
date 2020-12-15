package com.anji.sp.controller;

import com.anji.sp.aspect.PreSpAuthorize;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpApplicationVO;
import com.anji.sp.model.vo.SpRoleMenuInsertVO;
import com.anji.sp.model.vo.SpRoleVO;
import com.anji.sp.service.SpRoleService;
import com.anji.sp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kean on 2020/6/23.
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色接口")
public class SpRoleController {

    @Autowired
    private SpRoleService spRoleService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询所有角色", httpMethod = "POST")
    @PostMapping("/select/v1")
    public ResponseModel select() {
        return spRoleService.selectRoleList();
    }

    @ApiOperation(value = "根据角色id删除角色", httpMethod = "POST")
    @PostMapping("/deleteByRoleId/v1")
    @PreSpAuthorize("system:user:members")
    public ResponseModel deleteByRoleId(@RequestBody SpRoleVO spRoleVO) {
        return spRoleService.deleteRoleByRoleId(spRoleVO);
    }

    @ApiOperation(value = "新增角色及菜单", httpMethod = "POST")
    @PostMapping("/insertRoleAndMenu/v1")
    @PreSpAuthorize("system:user:members")
    public ResponseModel insertRoleAndMenu(@RequestBody SpRoleMenuInsertVO spRoleMenuInsertVO) {
        return spRoleService.insertRoleAndMenu(spRoleMenuInsertVO);
    }

    @ApiOperation(value = "根据角色id更新角色", httpMethod = "POST")
    @PostMapping("/update/v1")
    @PreSpAuthorize("system:user:members")
    public ResponseModel update(@RequestBody SpRoleVO spRoleVO) {
        return spRoleService.updateRoleByRoleId(spRoleVO);
    }

    @ApiOperation(value = "根据项目id查询未加入对应应用管理的用户", httpMethod = "POST")
    @PostMapping("/selectNoJoinApplicationByAppId/v1")
    @PreSpAuthorize("system:user:members")
    public ResponseModel selectNoJoinApplicationByAppId(@RequestBody SpApplicationVO spApplicationVO) {
        return userService.selectNoJoinApplicationByAppId(spApplicationVO);
    }
}
