package com.anji.sp.config;

import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by raodeming on 2020/6/30.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionConfig {

    @ExceptionHandler(Exception.class)
    public ResponseModel apiExceptionHandler(Exception ex) {
        log.error("error", ex);
        return ResponseModel.exceptionMsg(ex.getMessage());
    }

    /**
     * @param exception
     * @return
     * @throws Exception 方法访问权限不足异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Object accessDeniedExceptionHandler(AccessDeniedException exception) throws Exception {
        log.error(exception.getMessage());
        return ResponseModel.errorMsg(RepCodeEnum.AUTH_ERROR);
    }

}
