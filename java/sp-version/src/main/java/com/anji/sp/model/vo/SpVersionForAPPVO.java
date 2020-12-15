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
@ApiModel("给APP的版本信息")
public class SpVersionForAPPVO implements Serializable {
    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("是否强制更新")
    private boolean mustUpdate;

    @ApiModelProperty("是否显示更新")
    private boolean showUpdate;

    @ApiModelProperty(value = "更新日志", required = true)
    private String updateLog;


}