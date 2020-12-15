package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色与菜单新增对应关系
 *
 * @author kean 2020-06-27
 */
@Data
@ApiModel("角色与菜单新增对应关系")
public class SpRoleMenuInsertVO implements Serializable {

    @ApiModelProperty("用户角色关系表")
    private List<Long> menuIds;
    @ApiModelProperty("角色名称")
    private String roleName;

    public SpRoleMenuInsertVO() {
    }

}