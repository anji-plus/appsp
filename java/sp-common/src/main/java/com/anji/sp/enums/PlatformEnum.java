package com.anji.sp.enums;

/**
 * 用户状态
 */
public enum PlatformEnum {
    iOS("iOS", "iOS"), Android("Android", "Android");

    private final String code;
    private final String info;

    PlatformEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
