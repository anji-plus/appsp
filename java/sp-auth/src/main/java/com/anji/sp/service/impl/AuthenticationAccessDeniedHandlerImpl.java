package com.anji.sp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.util.ServletUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限认证失败处理类 返回未授权
 */
@Component
public class AuthenticationAccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String auth = JSONObject.toJSONString(ResponseModel.errorMsg(RepCodeEnum.AUTH_ERROR));
        ServletUtils.renderString(response, auth);
    }
}
