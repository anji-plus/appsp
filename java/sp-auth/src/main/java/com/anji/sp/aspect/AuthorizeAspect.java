package com.anji.sp.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anji.sp.model.LoginUser;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.SpUserAppRoleService;
import com.anji.sp.util.SecurityUtils;
import com.anji.sp.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created by raodeming on 2020/6/29.
 * 所有权限标识
 */
@Aspect
@Component
@Slf4j
public class AuthorizeAspect {
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    @Autowired
    private SpUserAppRoleService permissionService;

    // 配置织入点
    @Pointcut("@annotation(com.anji.sp.aspect.PreSpAuthorize)")
    public void authorizePointCut() {
    }

    @Around("authorizePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser.getUser().getIsAdmin() != 1) {
            // 获得注解
            PreSpAuthorize preSpAuthorize = getAnnotationLog(point);
            if (preSpAuthorize == null) {
                throw new AccessDeniedException("权限不足");
            }
            String params = argsArrayToString(point.getArgs());
            //解析请求参数是否含有appId
            long appId = JSONObject.parseObject(params).getLongValue("appId");
            //权限码
            String value = preSpAuthorize.value();
            //SpUserVO spUserVO
            SpUserVO spUserVO = new SpUserVO();
            spUserVO.setUserId(loginUser.getUser().getUserId());
            spUserVO.setIsAdmin(loginUser.getUser().getIsAdmin());
            Map<Long, Set<String>> longSetMap = permissionService.selectUserMenuPerms(spUserVO);
            loginUser.setPermissions(longSetMap);
            Set<String> strings = loginUser.getPermissions().get(appId);
            if (!hasPermissions(strings, value)) {
                throw new AccessDeniedException("权限不足");
            }
        }

        //执行方法
        return point.proceed();
    }


    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermissions(Set<String> permissions, String permission) {
        if (null == permission || null == permissions) {
            return false;
        }
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private PreSpAuthorize getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(PreSpAuthorize.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}