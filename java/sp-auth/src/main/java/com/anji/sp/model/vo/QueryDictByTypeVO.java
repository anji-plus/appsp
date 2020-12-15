package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典表
 *
 * @author Kean 2020-06-23
 */
@Data
@ApiModel("字典表")
public class QueryDictByTypeVO implements Serializable {
    @ApiModelProperty(value = "传入字段 tpye: 1、IOS_VERSION iOS版本号 2、ANDROID_VERSION Android版本号 3、TEMPLATE 模板类型 ", required = true)
    private String type;
    @ApiModelProperty(value = "数据值", required = true)
    private String value;

    @ApiModelProperty(value = "校验类型： 1 编辑  2 删除", required = true)
    private Integer checkType;

}