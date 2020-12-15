package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpAppReqDataVO;
import com.anji.sp.service.SpAppReqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by raodeming on 2020/6/23.
 */
@RestController
@RequestMapping("/phone")
@Api(tags = "手机端请求接口")
public class SpAppReqController {

    @Autowired
    private SpAppReqService spAppReqService;

    @ApiOperation(value = "初始化保存用户设备信息", httpMethod = "POST")
    @PostMapping("/deviceInit")
    public ResponseModel deviceInit(@RequestBody SpAppReqDataVO spAppReqDataVO, HttpServletRequest request) {
        return spAppReqService.deviceInit(spAppReqDataVO, request);
    }

    @ApiOperation(value = "获取APP 版本 信息", httpMethod = "POST")
    @PostMapping("/appVersion")
    public ResponseModel getAppVersion(@RequestBody SpAppReqDataVO spAppReqDataVO, HttpServletRequest request) {
        return spAppReqService.getAppVersion(spAppReqDataVO, request);
    }

    @ApiOperation(value = "获取APP 公告 信息", httpMethod = "POST")
    @PostMapping("/appNotice")
    public ResponseModel getAppNotice(@RequestBody SpAppReqDataVO spAppReqDataVO, HttpServletRequest request) {
        return spAppReqService.getAppNotice(spAppReqDataVO, request);
    }
}
