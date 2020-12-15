package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单
 *
 * @author kean 2020-06-25
 */
@Data
@ApiModel("菜单")
public class SpMenuVO implements Serializable {

    @ApiModelProperty("menu_id")
    private Long menuId;

    @ApiModelProperty(value = "父菜单id，一级菜单为0", required = true)
    private Long parentId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单url")
    private String url;

    @ApiModelProperty("授权(多个用逗号分隔，如：user:list，user:create)")
    private String perms;

    @ApiModelProperty("类型  0：目录  1：菜单  2：按钮")
    private Integer type;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer orderNum;

    public SpMenuVO() {
    }

}