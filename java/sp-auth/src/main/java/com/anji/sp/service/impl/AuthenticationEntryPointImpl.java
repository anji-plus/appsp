package com.anji.sp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.util.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * token口令失败处理
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        String auth = JSONObject.toJSONString(ResponseModel.errorMsg(RepCodeEnum.PASSWORD_VERIFICATION_FAILED));
        ServletUtils.renderString(response, auth);
    }
}
