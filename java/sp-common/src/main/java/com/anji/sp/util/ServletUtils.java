package com.anji.sp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 客户端工具类
 */
@Slf4j
public class ServletUtils {
    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {
//        return Convert.toStr(getRequest().getParameter(name), defaultValue);
        return null;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public static String getRequestString(HttpServletRequest request) {
        String result = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8");
            // 包装request的输入流
            BufferedReader br = new BufferedReader(inputStreamReader);
            // 缓冲字符
            StringBuffer sb = new StringBuffer("");
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close(); // 关闭缓冲流
            result = sb.toString(); // 转换成字符
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (Exception e) {
                log.error("close inputStreamReader error:{}", e);
            }
        }
        return result;
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
//        return Convert.toInt(getRequest().getParameter(name));
        return null;
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
//        return Convert.toInt(getRequest().getParameter(name), defaultValue);
        return null;
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
        response.setCharacterEncoding(Constants.UTF8);
        response.getWriter().print(string);
    }

}
