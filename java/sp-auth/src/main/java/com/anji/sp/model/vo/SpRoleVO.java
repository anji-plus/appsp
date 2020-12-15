package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色
 *
 * @author Kean 2020-06-25
 */
@Data
@ApiModel("角色")
public class SpRoleVO implements Serializable {

    @ApiModelProperty("role_id")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色标识")
    private String roleSign;

    public SpRoleVO() {
    }

}