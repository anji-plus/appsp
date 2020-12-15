package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公告管理
 *
 * @author kean 2020-06-30
 */
@Data
@ApiModel("返回app公告信息")
public class SpNoticeForAppVO implements Serializable {

    @ApiModelProperty("公告名称")
    private String name;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("公告内容")
    private String details;

    @ApiModelProperty("模板类型（来自字典表）")
    private String templateType;

    @ApiModelProperty("模板类型名")
    private String templateTypeName;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    public SpNoticeForAppVO() {
    }

}