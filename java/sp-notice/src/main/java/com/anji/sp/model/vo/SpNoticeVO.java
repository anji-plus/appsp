package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公告管理
 *
 * @author kean 2020-06-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("公告管理")
public class SpNoticeVO extends BaseVO implements Serializable {

    @ApiModelProperty(value = "编号", required = true)
    private Long id;

    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

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

    @ApiModelProperty("效果类型（来自字典表）")
    private Integer resultType;

    @ApiModelProperty("效果类型Name")
    private String resultTypeName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    public SpNoticeVO() {
    }

}