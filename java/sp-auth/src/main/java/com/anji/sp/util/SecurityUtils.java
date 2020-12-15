package com.anji.sp.util;

import com.anji.sp.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

/**
 * 安全服务工具类
 */
public class SecurityUtils {
    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户id
     **/
    public static Long getUserId() {

        if (Objects.isNull(getLoginUser())
                || Objects.isNull(getLoginUser().getUser())
                || Objects.isNull(getLoginUser().getUser().getUserId())) {
            return 1L;
        }


        return Objects.isNull(getLoginUser().getUser().getUserId()) ? 0L : getLoginUser().getUser().getUserId();
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        Object principal = getAuthentication().getPrincipal();
        if (Objects.isNull(principal) || !(principal instanceof LoginUser)) {
            return null;
        }
        return (LoginUser) principal;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        return getLoginUser().getUser().getIsAdmin() == 1;
    }

    public static void main(String[] args) {
        try {
            //前端加密
            String content = "123456";
            System.out.println("加密前：" + content);
            long start1 = System.currentTimeMillis();
            String encrypt = AESUtil.aesEncrypt(content);
            long end1 = System.currentTimeMillis();
            System.out.println("加密时间：" + (end1 - start1));
            System.out.println("加密后：" + encrypt);

            //后端
            long start = System.currentTimeMillis();
            String decrypt = AESUtil.aesDecrypt(encrypt);
            long end = System.currentTimeMillis();
            System.out.println("解密时间：" + (end - start));
            System.out.println("解密后：" + decrypt);


            String s = encryptPassword(decrypt);
            System.out.println("加密: " + s);
            boolean b = matchesPassword("123456", s);
            System.out.println(b);


            String decrypt1 = AESUtil.aesDecrypt("encrypt", null);

            System.out.println("decrypt1: "+decrypt1);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("解密失败");
        }




    }

}
