package com.anji.sp.util;


import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kean
 * @create 2020-04-13 1:31
 */
@Slf4j
public class IPUntils {
    // 静态属性,属于类 ,对于任何对象都是唯一的
    private static IPUntils instance;
    private static DbSearcher searcherInstance;
//    private static String DB_PATH = "/app/ip2region.db";
    private static String DB_PATH = IPUntils.class.getResource("/ip/ip2region.db").getPath();

    // 私有化构造方法,让外部不可以随意的创建对象
    private IPUntils() {
        if (null == searcherInstance) {
            try {
                DbConfig config = new DbConfig();
                searcherInstance = new DbSearcher(config, DB_PATH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 提供公共的方法，获取唯一的实例对象
    public synchronized static IPUntils getInstance() {
        if (null == instance) {
            instance = new IPUntils();
        }
        return instance;
    }


    public static void main(String[] args) throws Exception {
//        System.out.println(IPUntils.getInterIP1());
//        System.out.println(IPUntils.getInterIP2());
//        System.out.println(IPUntils.getOutIPV4());
        List<String> isp = new ArrayList<>();
        isp.add("222.66.93.20");
        isp.add("117.136.8.118");
        isp.add("124.77.91.195");
        isp.add("222.177.24.36");
        isp.add("58.240.94.147");
        isp.add("121.36.32.52");
        isp.addAll(isp);
        isp.addAll(isp);
        isp.addAll(isp);
        for (String ip : isp) {
            System.out.println(IPUntils.getInstance().getCityInstanceInfo(ip));
        }
        System.out.println("-------");
        for (String ip : isp) {
            System.out.println(IPUntils.getInstance().getCityInfo(ip));
        }
    }

    /**
     * @Description：获取客户端的IP
     * @author zrt
     * @date 2018年9月22日 上午10:39:44
     */
    public static String getIpAddress(HttpServletRequest request) {
        //注意本地测试时，浏览器请求不要用localhost，要用本机IP访问项目地址，不然这里取不到ip
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 单例查询 file 不close 弊端：无法在服务器动态更新.db文件
     * @param ip
     * @return
     */
    public String getCityInstanceInfo(String ip) {
        try {
            //查询算法
            int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcherInstance.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcherInstance.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcherInstance.getClass().getMethod("memorySearch", String.class);
                    break;
                default:
                    break;
            }

            DataBlock dataBlock = null;
            if (Util.isIpAddress(ip) == false) {
                log.info("Error: Invalid ip address");
                return null;
            }

            dataBlock = (DataBlock) method.invoke(searcherInstance, ip);
            String s = dataBlock.getRegion();
//            log.info("dataBlock {}, {}, {}, {}", dataBlock.getRegion(), dataBlock.getCityId(), dataBlock.getDataPtr(), dataBlock.toString());
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取ip地址城市信息
     * 例如 中国|0|上海|上海市|电信
     *
     * @param ip
     * @return
     */
    public String getCityInfo(String ip) {
        //查询算法
        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
        //DbSearcher.BINARY_ALGORITHM //Binary
        //DbSearcher.MEMORY_ALGORITYM //Memory
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, DB_PATH);
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
                default:
                    break;
            }

            DataBlock dataBlock = null;
            if (Util.isIpAddress(ip) == false) {
                log.info("Error: Invalid ip address");
                return null;
            }

            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String s = dataBlock.getRegion();
//            log.info("dataBlock {}, {}, {}, {}", dataBlock.getRegion(), dataBlock.getCityId(), dataBlock.getDataPtr(), dataBlock.toString());
            searcher.close();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

