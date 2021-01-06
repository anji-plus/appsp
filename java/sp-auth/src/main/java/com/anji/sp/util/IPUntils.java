package com.anji.sp.util;


import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kean
 * @create 2020-04-13 1:31
 */
@Slf4j
public class IPUntils {
    // 静态属性,属于类 ,对于任何对象都是唯一的
    private static IPUntils instance;
    private static DbSearcher searcher;

    // 私有化构造方法,让外部不可以随意的创建对象
    private IPUntils() {
        if (null == searcher) {
//            String dbPath = "/app/ip2region.db";
            String dbPath = IPUntils.class.getResource("/ip/ip2region.db").getPath();
            try {
                DbConfig config = new DbConfig();
                searcher = new DbSearcher(config, dbPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;

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
        System.out.println(IPUntils.getInstance().getCityInfo("125.41.185.183"));
        System.out.println(IPUntils.getInstance().getCityInfo("125.41.185.183"));
//        System.out.println(IPUntils.getCityInfo("125.41.185.183"));
    }


    public static String getInterIP1() throws Exception {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static String getInterIP2() throws SocketException {
        String localip = null; // 本地IP，如果没有配置外网IP则返回它
        String netip = null; // 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false; // 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) { // 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) { // 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public static String getOutIPV4() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        System.out.println(m);
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }
        return ip;
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
            //define the method
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

//            log.info("dataBlock {}, {}, {}, {}", dataBlock.getRegion(), dataBlock.getCityId(), dataBlock.getDataPtr(), dataBlock.toString());

            return dataBlock.getRegion();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

