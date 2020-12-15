package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by raodeming on 2020/6/22.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_user")
@ApiModel("用户")
public class SpUserPO extends BasePO {

    @ApiModelProperty("user_id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户的名字")
    private String name;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("0--不是admin 1--是admin")
    private Integer isAdmin;

    @ApiModelProperty("性别")
    private Long sex;

    @ApiModelProperty("pic_url")
    private Long picUrl;

    @ApiModelProperty("备注信息")
    private String remarks;

    public SpUserPO() {
    }

}
