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
@ApiModel("app请求log VO")
public class SpAppLogVO extends BaseVO implements Serializable {

    @ApiModelProperty(value = "应用唯一key", required = true)
    private String appKey;

    @ApiModelProperty(value = "设备唯一标识", required = true)
    private String deviceId;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("地域（中国|0|上海|上海市|电信）（0|0|0|内网ip|内网ip）")
    private String regional;

    @ApiModelProperty(value = "联网方式", required = true)
    private String netWorkStatus;

    @ApiModelProperty(value = "平台(ios/android)", required = true)
    private String platform;

    @ApiModelProperty(value = "高*宽", required = true)
    private String screenInfo;

    @ApiModelProperty(value = "设备品牌（例如：小米6，iphone 7plus）", required = true)
    private String brand;

    @ApiModelProperty(value = "版本名", required = true)
    private String versionName;

    @ApiModelProperty(value = "版本号", required = true)
    private String versionCode;

    @ApiModelProperty(value = "接口类型以id形式", required = true)
    private String interfaceType;

    @ApiModelProperty(value = "接口类型名（初始化，版本更新，公告)", required = true)
    private String interfaceTypeName;

    @ApiModelProperty(value = "系统版本例如：7.1.2  13.4.1", required = true)
    private String osVersion;

    @ApiModelProperty("sdk版本")
    private String sdkVersion;
}