package com.anji.sp.model;

import com.alibaba.fastjson.JSONObject;
import com.anji.sp.enums.RepCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ResponseModel implements Serializable {

    private static final long serialVersionUID = 8445617032523881407L;

    private String repCode;

    private String repMsg;

    private Object repData;

    public ResponseModel() {
        this.repCode = RepCodeEnum.SUCCESS.getCode();
    }

    public ResponseModel(RepCodeEnum repCodeEnum) {
        this.setRepCodeEnum(repCodeEnum);
    }

    //成功
    public static ResponseModel success() {
        return ResponseModel.successMsg("成功");
    }

    public static ResponseModel successMsg(String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepMsg(message);
        return responseModel;
    }

    public static ResponseModel successData(Object data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.SUCCESS.getCode());
        responseModel.setRepMsg("成功");
        responseModel.setRepData(data);
        return responseModel;
    }

    //失败
    public static ResponseModel errorMsg(RepCodeEnum message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCodeEnum(message);
        return responseModel;
    }

    public static ResponseModel errorMsg(String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.ERROR.getCode());
        responseModel.setRepMsg(message);
        return responseModel;
    }

    public static ResponseModel errorMsg(RepCodeEnum repCodeEnum, String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(repCodeEnum.getCode());
        responseModel.setRepMsg(message);
        return responseModel;
    }

    public static ResponseModel exceptionMsg(String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.EXCEPTION.getCode());
        responseModel.setRepMsg(RepCodeEnum.EXCEPTION.getDesc() + ": " + message);
        return responseModel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    public boolean isError() {
        return !isSuccess();
    }

    public boolean isSuccess() {
        if (this == null) {
            return false;
        }
        return StringUtils.equals(this.repCode, RepCodeEnum.SUCCESS.getCode());
    }

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public void setRepCodeEnum(RepCodeEnum repCodeEnum) {
        this.repCode = repCodeEnum.getCode();
        this.repMsg = repCodeEnum.getDesc();
    }

    public String getRepMsg() {
        return repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    public Object getRepData() {
        return repData;
    }

    public void setRepData(Object repData) {
        this.repData = repData;
    }
}
