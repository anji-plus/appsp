package com.anji.sp.util.file;

/**
 * is delete
 */
public enum FileNameEnum {
    VERSION_JSON("/version/", "版本更新和公告JSON文价路径"),
    APK_ANDROID("/apk/", "Android apk上传路径");

    private final String code;
    private final String info;

    FileNameEnum(String code, String info) {
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
