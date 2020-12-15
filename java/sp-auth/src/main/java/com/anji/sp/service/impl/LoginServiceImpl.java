package com.anji.sp.service.impl;

import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.exception.BadPasswordUpperLimitException;
import com.anji.sp.mapper.UserMapper;
import com.anji.sp.model.LoginUser;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpUserPO;
import com.anji.sp.model.vo.LoginVO;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.LoginService;
import com.anji.sp.service.TokenService;
import com.anji.sp.util.AESUtil;
import com.anji.sp.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by raodeming on 2020/6/22.
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private CaptchaService captchaService;

    @Override
    public ResponseModel login(SpUserVO spUserVO) {
        CaptchaVO captchaVO = new CaptchaVO();
        //getCaptchaVerification
        captchaVO.setCaptchaVerification(spUserVO.getCaptchaVerification());
        com.anji.captcha.model.common.ResponseModel verificationResponse = captchaService.verification(captchaVO);
        //验证码错误
        if (verificationResponse.isSuccess() == false) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setRepCode(verificationResponse.getRepCode());
            responseModel.setRepMsg(verificationResponse.getRepMsg());
            return responseModel;
        }
        // AES 解密
        String decrypt = "";
        try {
            decrypt = AESUtil.aesDecrypt(spUserVO.getPassword(), null);
        } catch (Exception e) {
            decrypt = "";
            log.info("密码校验失败： {}", e.getMessage());
        }
        spUserVO.setPassword(decrypt);

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            //用户名和密码进行组合成一个实例UsernamePasswordAuthenticationToken (一个Authentication接口的实例, 我们之前看到的).
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(spUserVO.getUsername(), spUserVO.getPassword()));
        } catch (Exception e) {
            if (e instanceof BadPasswordUpperLimitException) {
                return ResponseModel.errorMsg(RepCodeEnum.BAD_PASSWORD_UPPER_LIMIT);
            } else if (e instanceof BadCredentialsException) {
                return ResponseModel.errorMsg(RepCodeEnum.USER_NOT_EXIST_OR_PASSWORD_ERROR);
            } else {
                return ResponseModel.errorMsg(e.getMessage());
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        log.info("loginUser {}", loginUser);

        String token = tokenService.createToken(loginUser);
        log.info("loginUser {}", loginUser);
        // 生成token
        LoginVO loginVO = new LoginVO();
        loginVO.setIsAdmin(loginUser.getUser().getIsAdmin());
        loginVO.setName(loginUser.getUser().getName());
        loginVO.setToken(token);
        loginVO.setUserId(loginUser.getUser().getUserId());
        loginVO.setUsername(loginUser.getUser().getUsername());
        return ResponseModel.successData(loginVO);
    }

    @Override
    public ResponseModel test(SpUserVO spUserVO) {
        String username = SecurityUtils.getUsername();
        boolean admin = SecurityUtils.isAdmin();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SpUserPO> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        return ResponseModel.successData(userList);
    }
}
