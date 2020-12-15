package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by raodeming on 2020/6/22.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("用户应用角色关联用户信息")
public class SpUserAppInfoVO extends BaseVO implements Serializable {
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @ApiModelProperty(value = "应用ID", required = true)
    private Long appId;

    @ApiModelProperty("用户应用关联id")
    private Long userAppRoleId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty(value = "角色名", required = true)
    private String roleName;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty("用户的名字")
    private String name;
}
