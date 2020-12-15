package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpApplicationVO;
import com.anji.sp.service.SpApplicationService;
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
@RequestMapping("/app")
@Api(tags = "应用接口")
public class SpApplicationController {

    @Autowired
    private SpApplicationService spApplicationService;

    @ApiOperation(value = "查询所有应用", httpMethod = "POST")
    @PostMapping("/select/v1")
    public ResponseModel select() {
        return spApplicationService.queryApplicationList();
    }

    @ApiOperation(value = "分页查询所有应用", httpMethod = "POST")
    @PostMapping("/selectByPage/v1")
    public ResponseModel selectByPage(@RequestBody SpApplicationVO spApplicationVO) {
        return spApplicationService.queryApplicationByPage(spApplicationVO);
    }

    @ApiOperation(value = "新建应用", httpMethod = "POST")
    @PostMapping("/insert/v1")
    public ResponseModel insert(@RequestBody SpApplicationVO spApplicationVO) {
        return spApplicationService.insertApplication(spApplicationVO);
    }

    @ApiOperation(value = "更新应用名", httpMethod = "POST")
    @PostMapping("/update/v1")
    public ResponseModel update(@RequestBody SpApplicationVO spApplicationVO) {
        return spApplicationService.updateApplication(spApplicationVO);
    }

    @ApiOperation(value = "删除应用", httpMethod = "POST")
    @PostMapping("/delete/v1")
    public ResponseModel delete(@RequestBody SpApplicationVO spApplicationVO) {
        return spApplicationService.deleteApplication(spApplicationVO);
    }
}
