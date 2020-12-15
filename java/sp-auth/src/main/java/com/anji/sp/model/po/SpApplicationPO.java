package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用
 *
 * @author kean 2020-06-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_application")
@ApiModel("应用")
public class SpApplicationPO extends BasePO {

    @ApiModelProperty("app_id")
    @TableId(value = "app_id", type = IdType.INPUT)
    private Long appId;

    @ApiModelProperty("应用唯一key")
    private String appKey;

    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("公钥")
    private String publicKey;

    @ApiModelProperty("私钥")
    private String privateKey;

    public SpApplicationPO() {
    }
}