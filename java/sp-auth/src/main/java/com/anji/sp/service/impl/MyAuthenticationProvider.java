package com.anji.sp.service.impl;

import com.anji.sp.exception.BadPasswordUpperLimitException;
import com.anji.sp.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by raodeming on 2020/9/18.
 */
@Component
@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserDetailsServiceImpl userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        //前端传来的密码
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = userService.loadUserByUsername(name);
        //加密比对过程
        if (!new BCryptPasswordEncoder().matches(password, userDetails.getPassword())) {
            //如果校验上线
            if (checkPasswordUpperLimit(userDetails)) {
                throw new BadPasswordUpperLimitException("密码：" + name + "错误次数上限");
            }
            throw new BadCredentialsException("登录用户：" + name + " 密码不正确");
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
    }


    //校验上线
    private boolean checkPasswordUpperLimit(UserDetails userDetails) {
        String redisKey = String.format("RUNNING:LIMIT:CODE:%s", userDetails.getUsername());
        redisKey.replace(".", "_");
        //30分钟后清除
        long count = redisService.incr(redisKey, 1L, 3 * 60);
        log.info("count---->,{}", count);
        if (count > 6) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
