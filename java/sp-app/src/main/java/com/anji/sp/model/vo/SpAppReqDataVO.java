package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * app请求log VO
 *
 * @author kean 2020-08-28
 */
@Data
@ApiModel("app请求VO")
public class SpAppReqDataVO implements Serializable {
    @ApiModelProperty(value = "应用唯一key", required = true)
    private SpAppLogVO data;
    @ApiModelProperty(value = "加密结果", required = true)
    private String sign;

}