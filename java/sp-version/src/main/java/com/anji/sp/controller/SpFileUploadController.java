package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpUploadVO;
import com.anji.sp.service.SpUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公告管理
 *
 * @author zhudaijie
 * @date 2020/06/24
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "上传功能")
public class SpFileUploadController {

    @Autowired
    private SpUploadService spUploadService;

    /**
     * 文件上传
     * @param spUploadVO
     * @return
     */

    /**
     * 文件上传
     *
     * @param fileUpload 上传的文件
     * @param appId     appId
     * @return
     */
    @ApiOperation(value = "fiddle脚本上传", notes = "fiddle脚本上传")
    @PostMapping(value = "/uploadFile/v1")
    public ResponseModel uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload,
                                    @RequestParam("appId") Long appId) {
        SpUploadVO spUploadVO = new SpUploadVO();
        spUploadVO.setAppId(appId);
        spUploadVO.setFile(fileUpload);
        return spUploadService.uploadFile(spUploadVO);
    }
}

