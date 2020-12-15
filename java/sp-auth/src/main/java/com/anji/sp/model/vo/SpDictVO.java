package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 字典表
 *
 * @author Kean 2020-06-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("字典表")
public class SpDictVO extends BaseVO implements Serializable {

    @ApiModelProperty("编号")
    private Long id;

    @ApiModelProperty("标签名")
    private String name;

    @ApiModelProperty("数据值")
    private String value;

    @ApiModelProperty(value = "类型（android版本:  ANDROID_VERSION、ios版本 IOS_VERSION、公告类型 TEMPLATE、展示类型 RESULT）", required = true)
    private String type;

    @ApiModelProperty("描述（android版本、ios版本、公告类型、展示类型）")
    private String description;

    @ApiModelProperty("排序（升序）")
    private Long sort;

    public SpDictVO() {
    }

}