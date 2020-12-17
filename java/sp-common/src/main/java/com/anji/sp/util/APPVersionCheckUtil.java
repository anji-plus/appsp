package com.anji.sp.util;

public class APPVersionCheckUtil {
    /**
     * 判断是否为最新版本方法 将版本号根据.切分为int数组 比较
     *
     * @param oldVersion 本地版本号
     * @param newVersion 线上版本号
     * @return 是否为新版本
     * @throws IllegalArgumentException argument may not be null !
     */
    public static int compareAppVersion(String oldVersion, String newVersion) {
        System.out.println(oldVersion + "," + newVersion);

        if (oldVersion == null || newVersion == null) {
            return -1;
//            throw new IllegalArgumentException("argument may not be null !");
        }
        if (oldVersion.equals(newVersion)) {
            System.out.println("equal");
            return 0;
        }
        String[] oldArray = oldVersion.replaceAll("[^0-9.]", "").split("[.]");
        String[] newArray = newVersion.replaceAll("[^0-9.]", "").split("[.]");
        if (oldArray.length == 0 || newArray.length == 0) {
            return -1;
        }
        int length = oldArray.length < newArray.length ? oldArray.length : newArray.length;
        for (int i = 0; i < length; i++) {
            if ("".equals(newArray[i].trim()) || "".equals(oldArray[i].trim())) {
                return -1;
            }
            System.out.println(Integer.parseInt(newArray[i]) + " , " + Integer.parseInt(oldArray[i]));

            if (Integer.parseInt(newArray[i]) > Integer.parseInt(oldArray[i])) {
                System.out.println("t");
                return 1;
            } else if (Integer.parseInt(newArray[i]) < Integer.parseInt(oldArray[i])) {
                System.out.println("f");
                return -1;
            }
            // 相等 比较下一组值
        }
        //比较最后差异组数值
        if (oldArray.length < newArray.length) {
            System.out.println("l");
            if (Integer.parseInt(newArray[newArray.length - 1]) > 0) {
                return 1;
            }
            return -1;
        } else if (oldArray.length > newArray.length) {
            System.out.println("g");
            if (0 > Integer.parseInt(oldArray[oldArray.length - 1])) {
                return 1;
            }
            return -1;
        }
        return 1;
    }

    /**
     * 获取系统版本号
     *
     * @param osVersion
     * @return
     */
    public static int getOSVersion(String osVersion) {
        String[] oldArray = osVersion.replaceAll("[^0-9.]", "").split("[.]");
        if (oldArray.length > 0 && !("".equals(oldArray[0].trim()))) {
            return Integer.parseInt(oldArray[0].trim());
        }
        return 0;
    }


    /**
     * 是否是三位版本号
     *
     * @param version
     * @return
     */
    public static boolean isVersion(String version) {
        String[] p = version.split("\\.");
//        if (p.length != 3) {
//            return false;
//        }
        for (String pp : p) {
            int val = 0;
            try {
                val = Integer.valueOf(pp);
            } catch (Exception e) {
                System.out.println("Error: " + e);
                return false;
            }
            if (val < 0) {
                return false;
            }
        }

        return true;
    }


    /**
     * 字符串转换为int  非数字类型转换为0
     * @param str
     * @return
     */
    public static int strToInt(String str) {
        if (str.length() == 0)
            return 0;
        char[] chars = str.toCharArray();
        // 判断是否存在符号位
        int flag = 0;
        if (chars[0] == '+')
            flag = 1;
        else if (chars[0] == '-')
            flag = 2;
        int start = flag > 0 ? 1 : 0;
        int res = 0;// 保存结果
        for (int i = start; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {// 调用Character.isDigit(char)方法判断是否是数字，是返回True，否则False
                int temp = chars[i] - '0';
                res = res * 10 + temp;
            } else {
                return 0;
            }
        }
        return flag != 2 ? res : -res;

    }

    public static void main(String[] args) {
        System.out.println(strToInt(""));

        System.out.println(compareAppVersion("hh", "1.2.d"));

    }
}
