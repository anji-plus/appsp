package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * app请求log
 *
 * @author kean 2020-08-28
 */
@Data
@TableName("sp_app_log")
@ApiModel("app请求log")
public class SpAppLogPO extends BasePO {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("应用唯一key")
    private String appKey;

    @ApiModelProperty("设备唯一标识")
    private String deviceId;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("地域（中国|0|上海|上海市|电信）（0|0|0|内网ip|内网ip）")
    private String regional;

    @ApiModelProperty("联网方式")
    private String netWorkStatus;

    @ApiModelProperty("平台(ios/android)")
    private String platform;

    @ApiModelProperty("高*宽")
    private String screenInfo;

    @ApiModelProperty("设备品牌（例如：小米6，iphone 7plus）")
    private String brand;

    @ApiModelProperty("版本名")
    private String versionName;

    @ApiModelProperty("版本号")
    private String versionCode;

    @ApiModelProperty("接口类型以id形式")
    private String interfaceType;

    @ApiModelProperty("接口类型名（初始化，版本更新，公告)")
    private String interfaceTypeName;

    @ApiModelProperty("系统版本例如：7.1.2  13.4.1")
    private String osVersion;

    @ApiModelProperty("sdk版本")
    private String sdkVersion;

}