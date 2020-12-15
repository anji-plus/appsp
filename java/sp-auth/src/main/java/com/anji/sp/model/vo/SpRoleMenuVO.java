package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系VO
 *
 * @author kean 2020-06-27
 */
@Data
@ApiModel("角色与菜单对应关系VO")
public class SpRoleMenuVO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("菜单名称")
    private String menuName;

    public SpRoleMenuVO() {
    }

}