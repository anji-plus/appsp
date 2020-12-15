package com.anji.sp.service.impl;

import com.anji.sp.enums.IsDeleteEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.model.LoginUser;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.SpUserAppRoleService;
import com.anji.sp.service.UserService;
import com.anji.sp.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    //
    @Autowired
    private SpUserAppRoleService permissionService;

    /**
     * 根据用户名字获取用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SpUserVO user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (IsDeleteEnum.DELETE.getCode().equals(String.valueOf(user.getDeleteFlag()))) {
            log.info("登录用户：{} 已被删除.", username);
            throw new RuntimeException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(String.valueOf(user.getEnableFlag()))) {
            log.info("登录用户：{} 已被停用.", username);
            throw new RuntimeException("对不起，您的账号：" + username + " 已停用");
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SpUserVO user) {
        return new LoginUser(user, permissionService.selectUserMenuPerms(user));
    }
}
