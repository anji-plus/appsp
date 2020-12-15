package com.anji.sp.enums;

/**
 * is delete
 */
public enum IsDeleteEnum {
    NOT_DELETE("0", "未删除"), DELETE("1", "已删除");

    private final String code;
    private final String info;

    IsDeleteEnum(String code, String info) {
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
