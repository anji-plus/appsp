package com.anji.sp.enums;


import com.anji.sp.model.ResponseModel;

import java.text.MessageFormat;

/**
 * 返回应答码
 *
 * @author
 */
public enum RepCodeEnum {

    /**
     * 0001 - 0099 网关应答码
     */
    SUCCESS("0000", "成功"),
    ERROR("0001", "操作失败"),
    EXCEPTION("9999", "服务器内部异常"),


    BLANK_ERROR("0011", "{0}不能为空"),
    NULL_ERROR("0011", "{0}不能为空"),
    NOT_NULL_ERROR("0012", "{0}必须为空"),
    NOT_EXIST_ERROR("0013", "{0}数据库中不存在"),
    EXIST_ERROR("0014", "{0}数据库中已存在"),
    PARAM_TYPE_ERROR("0015", "{0}类型错误"),
    PARAM_FORMAT_ERROR("0016", "{0}格式错误"),


    /**=========================== 1xxxx base服务开始 ===================================*/
    /** 1001 - 1099 权限业务应答码 */

    /**
     * 1101 - 1199 角色业务应答码
     */
    VERSION_EXIST("1101", "版本已存在"),
    VERSION_INSERT_FAILURE("1102", "添加失败"),
    APP_EXIST("1103", "项目已存在"),
    APP_NOT_EXIST("1103", "项目不存在"),
    NOT_OPERATION("1104", "该用户不是管理员,无法操作"),


//    NOT_OPERATION("1104", "该用户不是管理员,无法操作"),
//    NOT_OPERATION("1104", "该用户不是管理员,无法操作"),

    /**
     * 1201 - 1299 用户业务应答码
     */
    LACK_DATA("1201", "数据缺失"),
    LACK_DATA_APP_KEY("1202", "appKey不能为空"),
    INVALID_FORMAT_APP_KEY("1203", "appKey校验失败"),
    LACK_DATA_DEVICE_ID("1204", "deviceId不能为空"),
    DATA_PARSING_INVALID("1205", "数据解析失败"),
    DATA_REQUEST_INVALID("1206", "请求数据与解析属于不一致"),
    OS_VERSION_INVALID("1207", "系统版本号不能为空"),
    APP_VERSION_INVALID("1208", "应用版本号不能为空"),


    USER_NOT_EXIST_OR_PASSWORD_ERROR("1204", "用户或密码不正确"),
    BAD_PASSWORD_UPPER_LIMIT("1210", "尝试登陆次数超过上限，请三分钟后再次登录"),

    PASSWORD_VERIFICATION_FAILED("0104", "口令校验失败"),
    AUTH_ERROR("0025", "权限校验失败");
    private String code;
    private String desc;

    RepCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return this.name();
    }

    /**
     * 将入参fieldNames与this.desc组合成错误信息
     * {fieldName}不能为空
     *
     * @param fieldNames
     * @return
     */
    public ResponseModel parseError(String... fieldNames) {
        ResponseModel errorMessage = new ResponseModel();
        String newDesc = MessageFormat.format(this.desc, fieldNames);

        errorMessage.setRepCode(this.code);
        errorMessage.setRepMsg(newDesc);
        return errorMessage;
    }
}
