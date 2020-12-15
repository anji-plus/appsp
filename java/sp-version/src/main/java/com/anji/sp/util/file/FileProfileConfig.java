package com.anji.sp.util.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author : kean_qi
 * create at:  2020/6/28  10:34 下午
 * @description: 文件路径配置
 */
@Configuration
public class FileProfileConfig {
    /**
     * 上传路径
     */

    private static String profile;

    public static String getProfile() {
        return profile;
    }

    @Value("${upload.filename:}")
    public void setProfile(String profilePath) {
        FileProfileConfig.profile = profilePath;
    }


    /**
     * aok文件下载路径地址
     */
    private static String apkUrl;

    public static String getApkUrl() {
        return apkUrl;
    }

    @Value("${file.apk.url:}")
    public void setApkUrl(String apkUrlPath) {
        FileProfileConfig.apkUrl = apkUrlPath;
    }


    /**
     * json文件下载路径地址
     */
    private static String jsonUrl;

    public static String getJsonUrl() {
        return jsonUrl;
    }

    @Value("${file.json.url:}")
    public void setJsonUrl(String jsonUrlPath) {
        FileProfileConfig.jsonUrl = jsonUrlPath;
    }


    /**
     * 版本更新及公告JSON文价路径
     *
     * @return
     */
    public static String getVersionPath() {
        return profile + FileNameEnum.VERSION_JSON.getCode();
    }

    /**
     * Android apk上传路径
     *
     * @return
     */
    public static String getAndroidAPKPath() {
        return profile + FileNameEnum.APK_ANDROID.getCode();
    }
}

