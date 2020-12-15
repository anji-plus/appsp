package com.anji.sp.service;

import com.anji.sp.model.LoginUser;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by raodeming on 2020/6/22.
 */
public interface TokenService {

    /**
     * 获取request请求身份信息
     *
     * @return 用户信息
     */
    LoginUser getLoginUser(HttpServletRequest request);

    /**
     * 根据token获取用户身份信息
     *
     * @return 用户信息
     */
    LoginUser getLoginUser(String token);

    /**
     * 设置用户身份信息
     */
    void setLoginUser(LoginUser loginUser);

    /**
     * 删除用户身份信息
     */
    void delLoginUser(String token);

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    String createToken(LoginUser loginUser);

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @return 令牌
     */
    void verifyToken(LoginUser loginUser);

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    void refreshToken(LoginUser loginUser);

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    String createToken(Map<String, Object> claims);

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    Claims parseToken(String token);

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    String getUsernameFromToken(String token);

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    String getToken(HttpServletRequest request);

    /**
     * 获取token key
     * @param uuid
     * @return
     */
    String getTokenKey(String uuid);
}
