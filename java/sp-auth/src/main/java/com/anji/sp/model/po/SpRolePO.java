package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色
 *
 * @author Kean 2020-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_role")
@ApiModel("角色")
public class SpRolePO extends BasePO {

    @ApiModelProperty("role_id")
    @TableId(value = "role_id", type = IdType.INPUT)
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色标识")
    private String roleSign;

    public SpRolePO() {
    }

}