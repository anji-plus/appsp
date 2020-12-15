package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户与应用与角色对应关系
 *
 * @author kean 2020-06-30
 */
@Data
@TableName("sp_user_app_role")
@ApiModel("用户与应用与角色对应关系")
public class SpUserAppRolePO implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    @ApiModelProperty(value = "角色id", required = true)
    private Long roleId;

    public SpUserAppRolePO() {
    }

}