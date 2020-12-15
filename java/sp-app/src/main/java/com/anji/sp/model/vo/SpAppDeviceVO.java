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
@ApiModel("app设备唯一标识表 VO")
public class SpAppDeviceVO extends BaseVO implements Serializable {

    @ApiModelProperty(value = "应用唯一key", required = true)
    private String appKey;

    @ApiModelProperty(value = "设备唯一标识", required = true)
    private String deviceId;

    @ApiModelProperty("平台(ios/android)")
    private String platform;
}