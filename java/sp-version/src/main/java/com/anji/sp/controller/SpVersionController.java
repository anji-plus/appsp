package com.anji.sp.controller;

import com.anji.sp.aspect.Log;
import com.anji.sp.aspect.PreSpAuthorize;
import com.anji.sp.enums.BusinessType;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpVersionVO;
import com.anji.sp.service.SpVersionService;
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
@RequestMapping("/version")
@Api(tags = "版本更新接口")
public class SpVersionController {

    @Autowired
    private SpVersionService spVersionService;

    @ApiOperation(value = "根据应用id查询所有版本信息", httpMethod = "POST")
    @PostMapping("/select/v1")
    @PreSpAuthorize("system:user:version")
    public ResponseModel select(@RequestBody SpVersionVO versionVO) {
        return spVersionService.getAppVersionByPlatformAndAppId(versionVO);
    }

    @ApiOperation(value = "版本新增", httpMethod = "POST")
    @PostMapping("/insert/v1")
    @PreSpAuthorize("system:user:version")
    @Log(title = "版本新增信息", businessType = BusinessType.INSERT)
    public ResponseModel insert(@RequestBody SpVersionVO versionVO) {
        return spVersionService.insertVersion(versionVO);
    }

    @ApiOperation(value = "版本编辑", httpMethod = "POST")
    @PostMapping("/update/v1")
    @PreSpAuthorize("system:user:version")
    @Log(title = "版本编辑信息", businessType = BusinessType.UPDATE)
    public ResponseModel update(@RequestBody SpVersionVO versionVO) {
        return spVersionService.updateVersion(versionVO);
    }

    @ApiOperation(value = "根据id启用/禁用版本", httpMethod = "POST")
    @PostMapping("/enable/v1")
    @PreSpAuthorize("system:user:version")
    @Log(title = "启用/禁用版本信息", businessType = BusinessType.UPDATE)
    public ResponseModel enable(@RequestBody SpVersionVO versionVO) {
        return spVersionService.enableVersion(versionVO);
    }

    @ApiOperation(value = "版本删除", httpMethod = "POST")
    @PostMapping("/delete/v1")
    @PreSpAuthorize("system:user:version")
    @Log(title = "删除版本信息", businessType = BusinessType.DELETE)
    public ResponseModel delete(@RequestBody SpVersionVO versionVO) {
        return spVersionService.deleteVersion(versionVO);
    }


    @ApiOperation(value = "查询是否可以新增版本", httpMethod = "POST")
    @PostMapping("/queryVersionState/v1")
    @PreSpAuthorize("system:user:version")
    public ResponseModel queryVersionState(@RequestBody SpVersionVO versionVO) {
        return spVersionService.queryVersionState(versionVO);
    }

}
