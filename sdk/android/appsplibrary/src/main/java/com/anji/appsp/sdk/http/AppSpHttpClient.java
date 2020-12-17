package com.anji.appsp.sdk.http;

import com.anji.appsp.sdk.AppSpLog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 请求处理类，用基本的HttpURLConnection
 * 避免依赖其他library
 * 我们已经处理了https的握手场景
 * </p>
 */
public class AppSpHttpClient {
    private static final String TAG = AppSpHttpClient.class.getSimpleName();

    public void request(String url, final AppSpCallBack appSpCallBack) {
        request(url, null, AppSpRequestMethod.POST, appSpCallBack);
    }

    public void request(String url, Object data, final AppSpCallBack appSpCallBack) {
        request(url, data, AppSpRequestMethod.POST, appSpCallBack);
    }

    public void request(String url, Object data, AppSpRequestMethod appSpRequestMethod, final AppSpCallBack appSpCallBack) {
        if (appSpCallBack == null) {
            return;
        }
        new RequestWrapper(url, data, appSpRequestMethod, appSpCallBack).requestWrapper();
    }

    class RequestWrapper {
        private String baseUrl;
        private Object params;
        private AppSpRequestMethod appSpRequestMethod;
        private AppSpCallBack appSpCallBack;

        public RequestWrapper(String baseUrl, Object params, AppSpRequestMethod appSpRequestMethod, AppSpCallBack appSpCallBack) {
            this.baseUrl = baseUrl;
            this.params = params;
            this.appSpRequestMethod = appSpRequestMethod;
            this.appSpCallBack = appSpCallBack;
        }


        Thread thread = new Thread() {
            @Override
            public void run() {
                requestSync();
            }
        };

        private void requestSync() {
            AppSpLog.d("请求地址 " + baseUrl);
            try {
                HttpURLConnection urlConn = getConnection();
                if (urlConn == null) {
                    return;
                }
                String paramsStr = params.toString();
                AppSpLog.d("请求内容 " + paramsStr);
                // 请求的参数转换为byte数组
                byte[] postData = paramsStr.getBytes();
                // 发送请求参数
                DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                dos.write(postData);
                dos.flush();
                dos.close();
                // 判断请求成功
                if (urlConn.getResponseCode() == 200) {
                    // 获取返回的数据
                    String result = streamToString(urlConn.getInputStream());
                    String respData = result;
                    AppSpLog.d("请求成功，结果为： " + result);
                    //appKey为空
                    requestSuccess(respData);
                } else {
                    //请求失败，将请求状态码返回
                    fail(String.valueOf(urlConn.getResponseCode()), urlConn.getResponseMessage());
                    AppSpLog.d("请求失败 ");
                }
                // 关闭连接
                urlConn.disconnect();
            } catch (Exception e) {
                AppSpLog.e("请求异常 " + e.toString());
                fail(AppSpRespCode.REQUEST_EXCEPTION, e.toString());
            }
        }

        public void requestWrapper() {
            thread.start();
        }

        private HttpURLConnection getConnection() {
            HttpURLConnection urlConn = null;
            try {
                // 新建一个URL对象
                URL url = new URL(baseUrl);
                trustAllHosts();
                // 打开一个HttpURLConnection连接
                HttpsURLConnection https;
                if (url.getProtocol().toLowerCase().equals("https")) {
                    https = (HttpsURLConnection) url.openConnection();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    urlConn = https;
                } else {
                    urlConn = (HttpURLConnection) url.openConnection();
                }
                // 设置连接超时时间
                urlConn.setConnectTimeout(10 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(10 * 1000);
                // Post请求必须设置允许输出 默认false
                urlConn.setDoOutput(true);
                //设置请求允许输入 默认是true
                urlConn.setDoInput(true);
                // Post请求不能使用缓存
                urlConn.setUseCaches(false);
                String method = "POST";
                // 默认为Post请求
                if (AppSpRequestMethod.POST == appSpRequestMethod) {
                    method = "POST";
                } else if (AppSpRequestMethod.GET == appSpRequestMethod) {
                    method = "GET";
                } else if (AppSpRequestMethod.PUT == appSpRequestMethod) {
                    method = "PUT";
                } else if (AppSpRequestMethod.DELETE == appSpRequestMethod) {
                    method = "DELETE";
                }
                urlConn.setRequestMethod(method);
                // 配置请求Content-Type
                urlConn.setRequestProperty("Content-Type", "application/json");
                // 开始连接
                urlConn.connect();
            } catch (Exception e) {

            }
            return urlConn;
        }

        private void fail(final String code, final String msg) {
            appSpCallBack.onError(code, msg);
        }

        private void requestSuccess(String data) {
            appSpCallBack.onSuccess(data);
        }

        final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        private void trustAllHosts() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    AppSpLog.d("checkClientTrusted");
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    AppSpLog.d("checkServerTrusted");
                }
            }};

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 将输入流转换成字符串
         *
         * @param is 从网络获取的输入流
         * @return
         */
        public String streamToString(InputStream is) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.close();
                is.close();
                byte[] byteArray = baos.toByteArray();
                return new String(byteArray);
            } catch (Exception e) {
                AppSpLog.e("从网络获取的输入流 请求异常 " + e.toString());
                return null;
            }
        }
    }


}



