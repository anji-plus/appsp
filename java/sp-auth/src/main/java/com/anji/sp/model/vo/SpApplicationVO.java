package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 应用
 *
 * @author kean 2020-06-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("应用表")
public class SpApplicationVO extends BaseVO implements Serializable {

    @ApiModelProperty("app_id")
    private Long appId;

    @ApiModelProperty("应用唯一key")
    private String appKey;

    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("公钥")
    private String publicKey;


    public SpApplicationVO() {
    }
}