package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by raodeming on 2020/6/22.
 */
@Data
@ApiModel("应用信息")
public class SpAppInfoVO implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("应用ID")
    private Long appId;

    @ApiModelProperty("app_key")
    private String appKey;

    @ApiModelProperty("用户的名字")
    private String name;

    @ApiModelProperty("操作日志信息")
    private String operationTitle;

    @ApiModelProperty("操作日志信息时间")
    private String operationTime;

    @ApiModelProperty("操作日志信息用户名")
    private String operationName;

}
