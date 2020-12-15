package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 版本管理
 *
 * @author kean 2020-06-26
 */
@Data
@ApiModel("给APP的版本临时信息")
public class SpVersionAppTempVO implements Serializable {

    @ApiModelProperty(value = "平台名称 iOS、Android", required = true)
    private String platform;

    @ApiModelProperty(value = "版本名称", required = true)
    private String versionName;

    @ApiModelProperty(value = "版本号", required = true)
    private String versionNumber;

    @ApiModelProperty(value = "更新日志", required = true)
    private String updateLog;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("版本配置(需要版本更新的版本号例如 10,11,12)")
    private String versionConfig;

    @ApiModelProperty("版本配置(需要版本更新的版本号例如 1.1.1,1.1.2,1.1.3)")
    private String needUpdateVersions;


}