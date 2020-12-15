package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by raodeming on 2020/6/22.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpUserVO extends BaseVO implements Serializable {


    @ApiModelProperty("user_id")
    private Long userId;

    private Long appId;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty("用户的名字")
    private String name;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("状态 0:禁用，1:正常")
    private boolean status;

    @ApiModelProperty("0--不是admin 1--是admin")
    private Integer isAdmin;

    @ApiModelProperty("性别")
    private Long sex;

    @ApiModelProperty("pic_url")
    private Long picUrl;

    @ApiModelProperty("token")
    private String token;


    @ApiModelProperty("captchaVerification")
    private String captchaVerification;

}
