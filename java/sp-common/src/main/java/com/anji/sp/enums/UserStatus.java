package com.anji.sp.enums;

/**
 * 用户状态
 */
public enum UserStatus {
    DISABLE("0", "已禁用"), OK("1", "已启用");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public Integer getIntegerCode() {
        return Integer.parseInt(code);
    }

    public String getInfo() {
        return info;
    }
}
