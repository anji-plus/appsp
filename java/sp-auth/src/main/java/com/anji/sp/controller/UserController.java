package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.UserService;
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
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "新增用户", httpMethod = "POST")
    @PostMapping("/addUser/v1")
    public ResponseModel addUser(@RequestBody SpUserVO spUserVO) {
        return userService.addUser(spUserVO);
    }


    @ApiOperation(value = "用户列表", httpMethod = "POST")
    @PostMapping("/queryByPage/v1")
    public ResponseModel queryByPage(@RequestBody SpUserVO spUserVO) {
        return userService.queryByPage(spUserVO);
    }

    @ApiOperation(value = "更新用户", httpMethod = "POST")
    @PostMapping("/updateUserById/v1")
    public ResponseModel updateUserById(@RequestBody SpUserVO spUserVO) {
        return userService.updateUserById(spUserVO);
    }

    @ApiOperation(value = "删除用户", httpMethod = "POST")
    @PostMapping("/deleteUserById/v1")
    public ResponseModel deleteUserById(@RequestBody SpUserVO spUserVO) {
        return userService.deleteUserById(spUserVO);
    }

}
