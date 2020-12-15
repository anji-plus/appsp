package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 版本管理
 *
 * @author kean 2020-06-26
 */
@Data
@ApiModel("文件上传VO")
public class SpUploadVO implements Serializable {

    @ApiModelProperty(value = "file", required = true)
    private MultipartFile file;

    @ApiModelProperty(value = "平台名称 iOS、Android", required = true)
    private String platform;

    @ApiModelProperty(value = "appKey")
    private String appKey;

    @ApiModelProperty(value = "appId", required = true)
    private Long appId;
}