## Android SDK����
* ��ȡservice/impl/controller/handler �Ľṹ������ģ�鹦����չ
* AppSpConfig ���࣬�����ṩ�����÷���

 ```java
	/**
     * @param host ����Ļ�����ַ
     */
    public AppSpConfig setHost(String host) {
        AppSpRequestUrl.Host = host;
        return appSpConfig;
    }

    /**
     * �����Ƿ��debug����
     *
     * @param debug true��ʾ��debug
     */
    public AppSpConfig setDebuggable(boolean debug) {
        AppSpLog.setDebugged(debug);
        return appSpConfig;
    }

    /**
     * ��ʼ��
     *
     * @param context ������ activity/fragment/view
     * @param appKey  ���ڱ�ʶ�ĸ�APP��Ψһ��ʶ
     */
    public AppSpConfig init(Context context, String appKey) {
        this.context = context;
        this.appKey = appKey;
        return appSpConfig;
    }
	
```

* �����ṩ����

```java
 
	/**
     * ��ȡ�豸��Ϣ
     */
    public void deviceInit() {
        AppSpVersionController appSpVersionController = new AppSpVersionController(context, appKey);
        appSpVersionController.initDevice();
    }

    /**
     * �汾���»ص�
     *
     * @param iAppSpVersionCallback
     */
    public void getVersion(IAppSpVersionCallback iAppSpVersionCallback) {
        AppSpVersionController appSpVersionController = new AppSpVersionController(context, appKey);
        appSpVersionController.getVersion(iAppSpVersionCallback);
    }

    /**
     * ����ص�
     *
     * @param iAppSpNoticeCallback
     */
    public void getNotice(IAppSpNoticeCallback iAppSpNoticeCallback) {
        AppSpNoticeController appSpNoticeController = new AppSpNoticeController(context, appKey);
        appSpNoticeController.getNotice(iAppSpNoticeCallback);
    }
	
```
### ����RSA����

* ��RSAUtil.java

```java
	/**
     * RSA����
     *
     * @param data      ����������
     * @param publicKey ��Կ
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
        // �����ݷֶμ���
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
        // ��ȡ��������ʹ��base64���б���,����UTF-8Ϊ��׼ת�����ַ���
        // ���ܺ���ַ���
        return Base64Util.encode(encryptedData);
    }
```

### �豸��Ϣ��ȡ
* ��Ϊ������ܾ���һ���Եģ����Է���versionģ��
* �ӿ�
```java
public interface IAppSpVersionService {
    //��ȡ�汾��Ϣ
    void getVersion();
}
```

* ʵ��

```java
	@Override
    public void initDevice() {
		//appKey����Ϊ��
        if(TextUtils.isEmpty(appKey)){
            AppSpLog.e("initDevice Appkey is null or empty");
            return;
        }
		//�����������
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
		//����
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

### �汾��Ϣ��ȡ
* �ӿ�
```java
public interface IAppSpVersionService {
    //��ȡ�汾��Ϣ
    void getVersion();
}
```

* ʵ��

```java
	@Override
    public void getVersion() {
		//appKey����Ϊ��
        if(TextUtils.isEmpty(appKey)){
            AppSpLog.e("getVersion Appkey is null or empty");
            return;
        }
		//�����������
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
		//����
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.getAppVersion, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
				//���ݽ���
                if (appSpVersionHandler != null) {
                    appSpVersionHandler.handleVersionSuccess(data);
                }
            }

            @Override
            public void onError(String code, String msg) {
				//���ݽ���
                if (appSpVersionHandler != null) {
                    appSpVersionHandler.handleUpdateException(code, msg);
                }
            }
        });
    }
```

* ���ݽ��������AppSpVersionHandler.java

```java
	/**
     * �汾���»�ȡ���ݳɹ�
     * json����
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
                    AppSpLog.d("�汾���·��ؿͻ������� " + spVersionModel);
					//������ظ�������
                    appSpVersionCallback.update(spVersionModel);
                } catch (Exception e) {
                    AppSpLog.d("�汾���·��ؿͻ������� Exception e " + e.toString());
                }
            }
        }

    }
	
	/**
     * �汾�����쳣����
     *
     * @param code
     * @param msg
     */
    public void handleUpdateException(String code, String msg) {
        if (appSpVersionCallback != null) {
            synchronized (appSpVersionCallback) {
                //Update error
				//������ظ�������
                appSpVersionCallback.error(code, msg);
            }
        }
    }
	
```

### ������Ϣ��ȡ
* �ӿ�

```java
public interface IAppSpNoticeService {
    void getNotice();
}
```

* ʵ��

```java
	@Override
    public void getNotice() {
		//appKey��Ϊ��
        if (TextUtils.isEmpty(appKey)) {
            AppSpLog.e("getNotice Appkey is null or empty");
            return;
        }
		//�����������
        AppSpPostData appSpPostData = getPostEncryptData();
        AppSpHttpClient client = new AppSpHttpClient();
        client.request(AppSpRequestUrl.Host + AppSpRequestUrl.getAppNotice, appSpPostData, new AppSpCallBack() {
            @Override
            public void onSuccess(String data) {
				//���ݴ���
                if (appSpNoticeHandler != null) {
                    appSpNoticeHandler.handleNoticeSuccess(data);
                }
            }

            @Override
            public void onError(String code, String msg) {
				//���ݴ���
                if (appSpNoticeHandler != null) {
                    appSpNoticeHandler.handleNoticeExcption(code, msg);
                }
            }
        });
    }
```

* ���ݽ��������AppSpNoticeHandler.java
```java
	/**
     * �����ȡ���ݳɹ�
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
                    AppSpLog.d("֪ͨ���ؿͻ������� " + spVersionModel);
                } catch (Exception e) {
                    AppSpLog.d("֪ͨ���ؿͻ������� Exception e " + e.toString());
                }

            }
        }
    }

    /**
     * �����쳣����
     *
     * @param code
     * @param msg
     */
    public void handleNoticeExcption(String code, String msg) {
        if (appSpNoticeCallback != null) {
            synchronized (appSpNoticeCallback) {
				//������ظ�������
                appSpNoticeCallback.error(code, msg);
            }
        }
    }
```