package com.anji.sp.util.file;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : kean_qi
 * create at:  2020/6/28  9:50 下午
 * @description: 文件处理工具类
 */
@Slf4j
public class FileUtils {

    public static String writeToJson(String platform, String appKey, String content) {

        String folderName = "";
        String fileName = appKey.concat("_").concat(platform).concat(".json");
        if ("iOS".contains(platform) || "Android".contains(platform)) {
            folderName = FileProfileConfig.getVersionPath();
            return fileWrite(folderName.concat(fileName), fileName, content);
        } else {
            return null;
        }

    }

    /**
     * 写入文件到对应目录
     *
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static String fileWrite(String fileName, String profileName, String content) {
        try {
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
                log.info("文件创建成功:  " + file.getParentFile().getName());
            }
            FileOutputStream fop = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            // get the content in bytes
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            log.info("写入成功:  " + fileName);
        } catch (Exception e) {
            log.error("处理异常", e);
            return null;
        }

        return FileProfileConfig.getJsonUrl().concat(profileName);
    }


    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        //存在则删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String fileNameConfig = fileName;
        if (agent.contains("MSIE")) { // IE浏览器
            fileNameConfig = URLEncoder.encode(fileNameConfig, "utf-8");
            fileNameConfig = fileNameConfig.replace("+", " ");
        } else if (agent.contains("Firefox")) { // 火狐浏览器
            fileNameConfig = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) { // google浏览器
            fileNameConfig = URLEncoder.encode(fileNameConfig, "utf-8");
        } else { // 其它浏览器
            fileNameConfig = URLEncoder.encode(fileNameConfig, "utf-8");
        }
        return fileNameConfig;
    }

    /**
     * 获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
     * @param path
     * @return
     */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }


}
