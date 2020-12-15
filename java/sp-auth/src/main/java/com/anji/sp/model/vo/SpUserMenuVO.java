package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by raodeming on 2020/6/22.
 */
@Data
@ApiModel("用户菜单权限信息")
public class SpUserMenuVO implements Serializable {
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @ApiModelProperty(value = "应用ID")
    private Long appId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty(value = "角色名", required = true)
    private String perms;

    @ApiModelProperty(value = "菜单名", required = true)
    private String menuName;

}
