package com.anji.sp.model.vo;

import lombok.Data;

/**
 * Created by raodeming on 2020/6/23.
 */
@Data
public class LoginVO {

    private Long userId;
    private String token;
    private String username;
    private String name;
    private int isAdmin;
}
