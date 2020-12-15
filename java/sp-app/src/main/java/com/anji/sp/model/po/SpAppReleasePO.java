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
@TableName("sp_app_release")
@ApiModel("app灰度发布用户信息表")
public class SpAppReleasePO extends BasePO {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("应用唯一key")
    private String appKey;

    @ApiModelProperty("设备唯一标识")
    private String deviceId;

    @ApiModelProperty("sdk版本")
    private String versionName;

}