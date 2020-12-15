package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpRoleMenuPO;
import com.anji.sp.model.vo.SpRoleMenuEditVO;
import com.anji.sp.service.SpRoleMenuService;
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
@RequestMapping("/roleMenu")
@Api(tags = "角色菜单关联接口")
public class SpRoleMenuController {

    @Autowired
    private SpRoleMenuService spRoleMenuService;

    @ApiOperation(value = "根据角色roleId查询对应菜单", httpMethod = "POST")
    @PostMapping("/selectByRoleId/v1")
    public ResponseModel selectByRoleId(@RequestBody SpRoleMenuPO spRoleMenuPO) {
        return spRoleMenuService.selectMenuListByRoleId(spRoleMenuPO);
    }

    @ApiOperation(value = "更新角色菜单关联表", httpMethod = "POST")
    @PostMapping("/update/v1")
    public ResponseModel update(@RequestBody SpRoleMenuEditVO spRoleMenuEditVO) {
        return spRoleMenuService.updateMenuToRole(spRoleMenuEditVO);
    }

    @ApiOperation(value = "根据角色id删除角色菜单表", httpMethod = "POST")
    @PostMapping("/delete/v1")
    public ResponseModel delete(@RequestBody SpRoleMenuPO spRoleMenuPO) {
        return spRoleMenuService.deleteMenuRoleByRoleId(spRoleMenuPO);
    }

}
