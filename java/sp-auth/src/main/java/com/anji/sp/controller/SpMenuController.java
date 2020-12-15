package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpMenuVO;
import com.anji.sp.service.SpMenuService;
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
@RequestMapping("/menu")
@Api(tags = "菜单接口")
public class SpMenuController {

    @Autowired
    private SpMenuService spMenuService;

    @ApiOperation(value = "查询所有菜单", httpMethod = "POST")
    @PostMapping("/select/v1")
    public ResponseModel select() {
        return spMenuService.selectMenuList();
    }


    @ApiOperation(value = "根据父级id查询菜单 0 或 1", httpMethod = "POST")
    @PostMapping("/selectByParentId/v1")
    public ResponseModel selectByParentId(@RequestBody SpMenuVO spMenuVO) {
        return spMenuService.selectMenuListByParentId(spMenuVO);
    }

}
