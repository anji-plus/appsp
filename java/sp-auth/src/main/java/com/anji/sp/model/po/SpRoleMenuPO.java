package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系PO
 *
 * @author kean 2020-06-27
 */
@Data
@TableName("sp_role_menu")
@ApiModel("角色与菜单对应关系PO")
public class SpRoleMenuPO implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("菜单id")
    private Long menuId;

    public SpRoleMenuPO() {
    }

}