## Android SDK解析
* 采取service/impl/controller/handler 的结构，方便模块功能扩展
* AppSpConfig 主类，对外提供可配置方法

 ```java
	/**
     * @param host 请求的基本地址
     */
    public AppSpConfig setHost(String host) {
        AppSpRequestUrl.Host = host;
        return appSpConfig;
    }

    /**
     * 设置是否打开debug开关
     *
     * @param debug true表示打开debug
     */
    public AppSpConfig setDebuggable(boolean debug) {
        AppSpLog.setDebugged(debug);
        return appSpConfig;
    }

    /**
     * 初始化
     *
     * @param context 可以是 activity/fragment/view
     * @param appKey  用于标识哪个APP，唯一标识
     */
    public AppSpConfig init(Context context, String appKey) {
        this.context = context;
        this.appKey = appKey;
        return appSpConfig;
    }
	
```

* 对外提供功能

```java
 
	/**
     * 获取设备信息
     */
    public void deviceInit() {
        AppSpVersionController appSpVersionController = new AppSpVersionController(context, appKey);
        appSpVersionController.initDevice();
    }

    /**
     * 版本更新回调
     *
     * @param iAppSpVersionCallback
     */
    public void getVersion(IAppSpVersionCallback iAppSpVersionCallback) {
        AppSpVersionController appSpVersionController = new AppSpVersionController(context, appKey);
        appSpVersionController.getVersion(iAppSpVersionCallback);
    }

    /**
     * 公告回调
     *
     * @param iAppSpNoticeCallback
     */
    public void getNotice(IAppSpNoticeCallback iAppSpNoticeCallback) {
        AppSpNoticeController appSpNoticeController = new AppSpNoticeController(context, appKey);
        appSpNoticeController.getNotice(iAppSpNoticeCallback);
    }
	
```
### 数据RSA加密

* 见RSAUtil.java

```java
	/**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64Util.encode(encryptedData);
    }
```

### 设备信息获取
* 因为这个功能就是一次性的，所以放在version模块
* 接口
```java
public interface IAppSpVersionService {
    //获取版本信息
    void getVersion();
}
```

* 实现

```java
	@Override
    public void initDevice() {
		//appKey不能为空
        if(TextUtils.isEmpty(appKey)){
            AppSpLog.e("initDevice Appkey is null or empty");
            return;
        }
		//请求基础数据
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
		//请求
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.initDevice, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
                AppSpLog.d("initDevice success");
            }

            @Override
            public void onError(String code, String msg) {
                AppSpLog.d("initDevice error");
            }
        });
    }
```

### 版本信息获取
* 接口
```java
public interface IAppSpVersionService {
    //获取版本信息
    void getVersion();
}
```

* 实现

```java
	@Override
    public void getVersion() {
		//appKey不能为空
        if(TextUtils.isEmpty(appKey)){
            AppSpLog.e("getVersion Appkey is null or empty");
            return;
        }
		//请求基础数据
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
		//请求
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.getAppVersion, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
				//数据解析
                if (appSpVersionHandler != null) {
                    appSpVersionHandler.handleVersionSuccess(data);
                }
            }

            @Override
            public void onError(String code, String msg) {
				//数据解析
                if (appSpVersionHandler != null) {
                    appSpVersionHandler.handleUpdateException(code, msg);
                }
            }
        });
    }
```

* 数据解析，详见AppSpVersionHandler.java

```java
	/**
     * 版本更新获取数据成功
     * json解析
     */
    public void handleVersionSuccess(String data) {
        if (appSpVersionCallback != null) {
            synchronized (appSpVersionCallback) {
                //Update
                try {
                    AppSpModel<AppSpVersion> spVersionModel = new AppSpModel<>();
                    JSONObject jsonObject = new JSONObject(data);
                    spVersionModel.setRepCode(getStringElement(jsonObject.opt("repCode")));
                    spVersionModel.setRepMsg(getStringElement(jsonObject.opt("repMsg")));

                    Object repDtaObj = jsonObject.opt("repData");
                    if (repDtaObj != null && !TextUtils.isEmpty(repDtaObj.toString())
                            && !"null".equalsIgnoreCase(repDtaObj.toString())) {
                        JSONObject repData = new JSONObject(repDtaObj.toString());
                        if (repData != null) {
                            AppSpVersion appSpVersion = new AppSpVersion();
                            appSpVersion.setDownloadUrl(getStringElement(repData.opt("downloadUrl")));
                            appSpVersion.setUpdateLog(getStringElement(repData.opt("updateLog")));
                            appSpVersion.setShowUpdate(getBooleanElement(repData.opt("showUpdate")));
                            appSpVersion.setMustUpdate(getBooleanElement(repData.opt("mustUpdate")));
                            spVersionModel.setRepData(appSpVersion);
                        }
                    }
                    AppSpLog.d("版本更新返回客户端数据 " + spVersionModel);
					//结果返回给调用者
                    appSpVersionCallback.update(spVersionModel);
                } catch (Exception e) {
                    AppSpLog.d("版本更新返回客户端数据 Exception e " + e.toString());
                }
            }
        }

    }
	
	/**
     * 版本更新异常处理
     *
     * @param code
     * @param msg
     */
    public void handleUpdateException(String code, String msg) {
        if (appSpVersionCallback != null) {
            synchronized (appSpVersionCallback) {
                //Update error
				//结果返回给调用者
                appSpVersionCallback.error(code, msg);
            }
        }
    }
	
```

### 公告信息获取
* 接口

```java
public interface IAppSpNoticeService {
    void getNotice();
}
```

* 实现

```java
	@Override
    public void getNotice() {
		//appKey不为空
        if (TextUtils.isEmpty(appKey)) {
            AppSpLog.e("getNotice Appkey is null or empty");
            return;
        }
		//请求基础数据
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.getAppNotice, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
				//数据处理
                if (appSpNoticeHandler != null) {
                    appSpNoticeHandler.handleNoticeSuccess(data);
                }
            }

            @Override
            public void onError(String code, String msg) {
				//数据处理
                if (appSpNoticeHandler != null) {
                    appSpNoticeHandler.handleNoticeExcption(code, msg);
                }
            }
        });
    }
```

* 数据解析，详见AppSpNoticeHandler.java
```java
	/**
     * 公告获取数据成功
     */
    public void handleNoticeSuccess(String data) {
        if (appSpNoticeCallback != null) {
            synchronized (appSpNoticeCallback) {
                //Notice
                try {
                    AppSpModel<List<AppSpNoticeModelItem>> spVersionModel = new AppSpModel<>();
                    JSONObject jsonObject = new JSONObject(data);
                    spVersionModel.setRepCode(getStringElement(jsonObject.opt("repCode")));
                    spVersionModel.setRepMsg(getStringElement(jsonObject.opt("repMsg")));
                    Object repDtaObj = jsonObject.opt("repData");
                    if (repDtaObj != null && !TextUtils.isEmpty(repDtaObj.toString())
                            && !"null".equalsIgnoreCase(repDtaObj.toString())) {
                        JSONArray repData = new JSONArray(repDtaObj.toString());
                        List<AppSpNoticeModelItem> items = new ArrayList<>();
                        if (repData != null) {
                            for (int i = 0; i < repData.length(); i++) {
                                AppSpNoticeModelItem item = new AppSpNoticeModelItem();
                                JSONObject value = repData.getJSONObject(i);
                                item.setTitle(getStringElement(value.opt("title")));
                                item.setDetails(getStringElement(value.opt("details")));
                                item.setTemplateType(getStringElement(value.opt("templateType")));
                                item.setTemplateTypeName(getStringElement(value.opt("templateTypeName")));
                                items.add(item);
                            }
                            spVersionModel.setRepData(items);
                            appSpNoticeCallback.notice(spVersionModel);
                        }
                    }
                    AppSpLog.d("通知返回客户端数据 " + spVersionModel);
                } catch (Exception e) {
                    AppSpLog.d("通知返回客户端数据 Exception e " + e.toString());
                }

            }
        }
    }

    /**
     * 公告异常处理
     *
     * @param code
     * @param msg
     */
    public void handleNoticeExcption(String code, String msg) {
        if (appSpNoticeCallback != null) {
            synchronized (appSpNoticeCallback) {
				//结果返回给调用者
                appSpNoticeCallback.error(code, msg);
            }
        }
    }
```