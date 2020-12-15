
package com.anji.sp.service.impl;

import com.anji.sp.model.LoginUser;
import com.anji.sp.service.RedisService;
import com.anji.sp.service.TokenService;
import com.anji.sp.util.Constants;
import com.anji.sp.util.RandCodeUtil;
import com.anji.sp.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    //一分钟毫秒数
    private static final Long MILLIS_MINUTE = 60 * 1000L;

    @Autowired
    private RedisService redisService;

    /**
     * 获取request请求身份信息
     *
     * @return 用户信息
     */
    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    @Override
    public LoginUser getLoginUser(String token) {
        log.info("token {}", token);
        if (StringUtils.isNotEmpty(token)) {
            //从令牌中获取数据声明
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            log.info("uuid {}", uuid);
            //查询Redis uey  比对是否一致  false  return null;
            if (StringUtils.isEmpty(uuid)) {
                return null;
            }
            String[] tokenUUIDs = uuid.split("_");
            if (StringUtils.isNotEmpty(tokenUUIDs)) {
                //LOGIN_USER_TOKEN_UUID_KEY_id
                String uuidKey = Constants.LOGIN_USER_TOKEN_UUID_KEY + "_" + tokenUUIDs[0];
                String redisToken = redisService.getCacheObject(uuidKey);
                log.info("redisToken {} ", redisToken);
                if (StringUtils.isEmpty(redisToken) || !redisToken.equals(uuid)) {
                    return null;
                }
            }

            //查询user信息
            String userKey = Constants.LOGIN_TOKEN_KEY + tokenUUIDs[0];
            log.info("userKey {}", userKey);
            LoginUser cacheObject = (LoginUser) redisService.getCacheObject(userKey);
            verifyToken(cacheObject);
            return cacheObject;
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    @Override
    public void setLoginUser(LoginUser loginUser) {
        log.info("setLoginUser: {}", loginUser);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    @Override
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisService.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    @Override
    public String createToken(LoginUser loginUser) {
        Long userId = loginUser.getUser().getUserId();
        //清除token
        String token = userId + "_" + RandCodeUtil.getUUID();
        //保存token 单点登录 token 的key 唯一  非单点登录不唯一
        updateLoginUserTokenUUIDKey(userId, token);
        loginUser.setToken(token);
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 更新登录用户token的UUIDKey
     */
    public void updateLoginUserTokenUUIDKey(Long userId, String token) {
        //清除Redis缓存
        // 根据uuid将loginUser缓存
        String uuidKey = Constants.LOGIN_USER_TOKEN_UUID_KEY + "_" + userId;
        redisService.setCacheObject(uuidKey, token, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @return 令牌
     */
    @Override
    public void verifyToken(LoginUser loginUser) {
        if (Objects.nonNull(loginUser)){
            long userExpireTime = loginUser.getExpireTime();
            long currentTime = System.currentTimeMillis();
            long gap = userExpireTime - currentTime;
//        log.info("刷新令牌： gap：{}, {}", gap, expireTime * MILLIS_MINUTE);
            //相差不足20分钟刷新
            if (gap <= (expireTime * MILLIS_MINUTE)) {
                refreshToken(loginUser);
            }
        }

    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    @Override
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser 用户信息缓存
        String userKey = Constants.LOGIN_TOKEN_KEY + loginUser.getUser().getUserId();
        updateLoginUserTokenUUIDKey(loginUser.getUser().getUserId(), loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    public static void main(String[] args) {
        String a = "login_tokens:1_808678c97e6f411b92e0b5b6d5a90a8f";
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    @Override
    public String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    @Override
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 获取token key
     * @param uuid
     * @return
     */
    @Override
    public String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
}
