package com.anji.sp.controller;

import com.anji.sp.aspect.Log;
import com.anji.sp.aspect.PreSpAuthorize;
import com.anji.sp.enums.BusinessType;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpNoticeVO;
import com.anji.sp.service.SpNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告管理
 *
 * @author zhudaijie
 * @date 2020/06/24
 */
@RestController
@RequestMapping("/notice")
@Api(tags = "公告接口")
public class SpNoticeController {

    @Autowired
    private SpNoticeService spNoticeService;

    @ApiOperation(value = "分页根据应用id分页查询公告信息", httpMethod = "POST")
    @PostMapping("/select/v1")
    @PreSpAuthorize("system:user:notice")
    public ResponseModel select(@RequestBody SpNoticeVO spNoticeVO) {
        return spNoticeService.getAppNoticeByAppId(spNoticeVO);
    }

    @ApiOperation(value = "新增公告信息", httpMethod = "POST")
    @PostMapping("/insert/v1")
    @PreSpAuthorize("system:user:notice")
    @Log(title = "新增公告信息", businessType = BusinessType.INSERT)
    public ResponseModel insert(@RequestBody SpNoticeVO spNoticeVO) {
        return spNoticeService.insertNotice(spNoticeVO);
    }

    @ApiOperation(value = "编辑公告信息", httpMethod = "POST")
    @PostMapping("/update/v1")
    @PreSpAuthorize("system:user:notice")
    @Log(title = "编辑公告信息", businessType = BusinessType.UPDATE)
    public ResponseModel update(@RequestBody SpNoticeVO spNoticeVO) {
        return spNoticeService.updateNotice(spNoticeVO);
    }

    @ApiOperation(value = "根据id启用/禁用公告信息", httpMethod = "POST")
    @PostMapping("/enable/v1")
    @PreSpAuthorize("system:user:notice")
    @Log(title = "启用/禁用公告信息", businessType = BusinessType.UPDATE)
    public ResponseModel enable(@RequestBody SpNoticeVO spNoticeVO) {
        return spNoticeService.enableNotice(spNoticeVO);
    }

    @ApiOperation(value = "删除公告信息", httpMethod = "POST")
    @PostMapping("/delete/v1")
    @PreSpAuthorize("system:user:notice")
    @Log(title = "删除公告信息", businessType = BusinessType.DELETE)
    public ResponseModel delete(@RequestBody SpNoticeVO spNoticeVO) {
        return spNoticeService.deleteNotice(spNoticeVO);
    }


}

