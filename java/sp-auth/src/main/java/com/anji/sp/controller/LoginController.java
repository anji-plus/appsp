package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by raodeming on 2020/6/22.
 */
@RestController
@Api(tags = "登录接口")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("/login/v1")
    public ResponseModel login(@RequestBody SpUserVO spUserVO) {
        return loginService.login(spUserVO);
    }

    @ApiOperation(value = "健康检查", httpMethod = "GET")
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

}
