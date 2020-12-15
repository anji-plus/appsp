package com.anji.sp.util.file;

import com.anji.sp.model.vo.SpVersionVO;
import com.anji.sp.util.file.exception.FileException;
import com.anji.sp.util.file.exception.FileNameLengthLimitExceededException;
import com.anji.sp.util.file.exception.FileSizeLimitExceededException;
import com.anji.sp.util.file.exception.InvalidFileExceededException;
import lombok.extern.slf4j.Slf4j;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author : kean_qi
 * create at:  2020/6/28  10:28 下午
 * @description: 文件上传工具类
 */
@Slf4j
public class FileUploadUtils {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1025;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 上传apk
     *
     * @param multipartFile 文件file
     * @param appKey        appkey
     */
    public static SpVersionVO uploadFile(MultipartFile multipartFile, String appKey) throws Exception {
        log.info("文件上传处理");
        SpVersionVO spVersionVO = new SpVersionVO();
        if (null == multipartFile || multipartFile.isEmpty()) {
            log.error("multiUpload文件对象为空");
            throw new FileException("upload.empty", new Object[]{"upload.empty"});
        }
        //文件名长度
        int fileNamelength = multipartFile.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = FilenameUtils.getExtension(fileName);
        String profileName = "";
        log.info("上传的后缀名为：" + suffixName);
        try {
            //文件大小校验
            long size = multipartFile.getSize();
            if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
                throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
            }
            // 解决中文问题，liunx下中文路径
            if ("apk".equals(suffixName)) {
                ApkMetaInfo apkMetaInfo = getApkInfo(multipartFile, appKey);
                ApkMeta apkMeta = apkMetaInfo.getApkMeta();
                spVersionVO.setVersionName(apkMeta.getVersionName());
                spVersionVO.setVersionNumber(apkMeta.getVersionCode() + "");
                profileName = apkMetaInfo.getProfileName();
                fileName = FileProfileConfig.getAndroidAPKPath().concat(profileName);
            } else {
                throw new InvalidFileExceededException("请上传正确的apk文件");
//                fileName = FileProfileConfig.getOtherPath() + appKey + UUID.randomUUID().toString();
            }
            // 创建临时文件
            File desc = new File(fileName);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            if (!desc.exists()) {
                desc.createNewFile();
            }
//                File tempfile = File.createTempFile(fileName, suffixName, null);
            //将文件转移到对应路径
            //把内存图片写入磁盘中
            multipartFile.transferTo(desc);
            // 删除文件
//            desc.deleteOnExit();
            //FileException
            log.info("上传文件成功 : {}", profileName);
            spVersionVO.setDownloadUrl(FileProfileConfig.getApkUrl().concat(profileName));
        } catch (FileException e) {
            log.error("处理异常", e);
            throw new FileException(e.getCode(), e.getArgs());
        } catch (Exception e) {
            throw new FileException("file.upload.error", new Object[]{e.getMessage()});
        }
        return spVersionVO;
    }

    public static void main(String[] args) {
        // .$|()[{^?*+\\  特殊字符作为分隔符时需要使用\\进行转义
//       String[] arr = "com.anji.plus.lsdclient".split("\\.");
//        System.out.println(arr[arr.length - 1]);
//        String s = "com.anji.plus.lsdclient".replace(".", "_");
//        System.out.println(s);

        //
//        String fileName = "/Users/kean_qi/Desktop/AllwaysWork/AllwaysGit/sp/upload/apk/";

//        List<String> files = FileUtils.getFiles(fileName);
//        for (String fileStr : files) {
//            System.out.println(fileStr);
//        }
//        System.out.println();
//        boolean b = FileUtils.deleteFile("/Users/kean_qi/Desktop/AllwaysWork/AllwaysGit/sp/upload/apk/lsdclient2030-1605668373753-25-94e76fcdd70c477c80e3ddcf98aa9b5a.apk");
//
//        if (b){
//            List<String> files1 = FileUtils.getFiles(fileName);
//            for (String fileStr : files1) {
//                System.out.println(fileStr);
//            }
//        }
//        List<String> s = new ArrayList<String>();
//        System.out.println(s.contains("a"));
//        for (String s1: s) {
//            System.out.println(s1);
//        }
    }

    static ApkMetaInfo getApkInfo(MultipartFile multipartFile, String appKey) throws Exception {
        File apkFile = MultipartFileToFile.multipartFileToFile(multipartFile);
        ApkFile apkParser = new ApkFile(apkFile);
        String xml = apkParser.getManifestXml();
        ApkMeta apkMeta = apkParser.getApkMeta();
        MultipartFileToFile.delteTempFile(apkFile);
        String[] packageNameArr = apkMeta.getPackageName().split("\\.");
        String packageName = packageNameArr[packageNameArr.length - 1];
        String versionName = apkMeta.getVersionName().replace(".", "");
        String versionCode = (apkMeta.getVersionCode() + "").replace(".", "");
        String dateTimeStr = String.valueOf(System.currentTimeMillis());
        // packageName+versionName - dateTimeStr - versionCode -appKey
        String profileName = packageName
                .concat(versionName)
                .concat("-").concat(dateTimeStr)
                .concat("-").concat(versionCode)
                .concat("-").concat(appKey)
                .concat(".apk");
        ApkMetaInfo apkMetaInfo = new ApkMetaInfo();
        apkMetaInfo.setApkMeta(apkMeta);
        apkMetaInfo.setProfileName(profileName);
        log.info("apk Info: {}", ToStringBuilder.reflectionToString(apkMetaInfo, ToStringStyle.JSON_STYLE));
        return apkMetaInfo;
    }

    static class ApkMetaInfo {
        ApkMeta apkMeta;
        String profileName;

        public ApkMeta getApkMeta() {
            return apkMeta;
        }

        public void setApkMeta(ApkMeta apkMeta) {
            this.apkMeta = apkMeta;
        }

        public String getProfileName() {
            return profileName;
        }

        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }
    }
}
