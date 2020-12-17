
# ���ٽ���ʹ��
## ��ʼ������
AppKey�Ļ�ȡ�� **�����ֲ�**

```java
public class AppContext extends Application {
    private static AppContext mInstance;
    //appkey����Appsp����Ӧ�����ɵ�
    public static final String appKey = "aadcfae6215a4e0f9bf5bc5edccb1045";

    public static AppContext getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
		//�ؼ�����
        AppSpConfig.getInstance()
                .init(this, appKey)
                //���޸Ļ��������ַ
                .setHost("http://open-appsp.anji-plus.com/sp/")
                //��ʽ�������Խ�ֹ��־�����ͨ��Tag APP-SP���˿���־
                .setDebuggable(BuildConfig.DEBUG ? true : false)
                //���Ҫ��ʼ���������������ᱨ��
                .deviceInit();
    }

}
```

## �汾���·���
* Ȩ��

```java
    <!-- apk�洢 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- �������� -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- apk���� -->
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <!-- 7.0+�ļ���ȡ com.anji.appsp.sdktest��Demo�İ���-->
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.anji.appsp.sdktest.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
```

* file_paths.xml����

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path
        name="files_root"
        path="Android/data/com.anji.appsp.sdktest/" />
    <external-path
        name="external_storage_root"
        path="." />
    <root-path
        name="root_path"
        path="" />
</paths>
```

* ����

 ```java
 private void checkVersion() {
        AppSpConfig.getInstance().setVersionUpdateCallback(new IAppSpVersionUpdateCallback() {
            @Override
            public void update(AppSpModel<AppSpVersion> spModel) {
                //ע�⣬��ǰ�����߳�
                //�ɹ����� TODO
            }

            @Override
            public void error(String code, String msg) {
               //ע�⣬��ǰ�����߳�
			   //ʧ�ܴ��� TODO
            }
        });
    }
 ```
 
**���󷵻����ݽṹ����**
 
**spModel** ��������
 
 ```java
  {
  	"repCode": "0000", //ҵ�񷵻��룬0000��ʾ�ɹ�
  	"repMsg": "�ɹ�",  //ҵ����־
  	"repData": {
  		"downloadUrl": "app���ص�ַ",
  		"mustUpdate": false, //�Ƿ�ǿ�Ƹ��£�trueΪǿ�Ƹ���
  		"showUpdate": true, //�Ƿ�����������
  		"updateLog": "������־"
  	}
  }
 ```
 
***
 
 | �ֶ�| ����| ˵�� |
 | :-----| :---- | :---- |
 | repCode |String | ҵ�񷵻��룬0000��ʾ�ɹ�  |
 | repMsg |String | ҵ����־���쳣��Ϣ |
 | repData | Object | ����ҵ�����ݰ���������� |
 
**repData** ��������
 
 | �ֶ�| ����| ˵�� |
 | :-----| :---- | :---- |
 | downloadUrl |String | app���ص�ַ |
 | mustUpdate | boolean | �Ƿ�ǿ�Ƹ��£�trueΪǿ�Ƹ���; falseΪ��ǿ�Ƹ��� |
 | showUpdate | boolean | �Ƿ���ʾ���£����������� |
 | updateLog |String | ������־ |
 
***

**errorInfo** ��������
 
 ```java
 repCode: �׳��쳣Code
 repMsg: �쳣��Ϣ
 
 ```
 
 | �ֶ�| ����| ˵�� |
 | :-----| :---- | :---- |
 | repCode |String | �׳��쳣Code �� 1001: �����쳣��1202: appKeyΪ�գ�1203: appKeyУ��ʧ�ܣ�1207: ϵͳ�汾�Ų���Ϊ�գ�1208: Ӧ�ð汾�Ų���Ϊ�� |
 | repMsg |String | �쳣��Ϣ |
 
 ### �������
   ```java
    private void checkNotice() {
        AppSpConfig.getInstance().getNotice(new IAppSpNoticeCallback() {
            @Override
            public void notice(AppSpModel<List<AppSpNoticeModelItem>> spModel) {
                //ע�⣬��ǰ�����߳�
                //�ɹ����� TODO
            }

            @Override
            public void error(String code, String msg) {
                //ע�⣬��ǰ�����߳�
                //ʧ�ܴ��� TODO
            }
        });

    }
   ```
**���󷵻����ݽṹ����**

***
**spModel** ��������

```java
 {
 	"repCode": "0000", //ҵ�񷵻��룬0000��ʾ�ɹ�
 	"repMsg": "�ɹ�", //ҵ����־
 	"repData": [ // ��������Ϊ List
 		{
	 		"title": "�������",
	 		"details": "��������",
	 		"templateType": "dialog", //�������ͣ� ������dialog�� ˮƽ������horizontal_scroll��
	 		"templateTypeName": "����"//����ģ������
 		}
 	]
 }
```

| �ֶ�| ����| ˵�� |
| :-----| :---- | :---- |
| repCode |String | ҵ�񷵻��룬0000��ʾ�ɹ�  |
| repMsg |String | ҵ����־���쳣��Ϣ |
| repData |Object | ����ҵ�����ݰ���������� |

**repData** ��������

| �ֶ�| ����| ˵�� |
| :-----| :---- | :---- |
| title |String | ������� |
| details |String | �������� |
| templateType |String | �������ͣ� ������dialog�� ˮƽ������horizontal_scroll��|
| templateTypeName |String | ����ģ������ |

***

**errorInfo** ��������
  
  ```java
  repCode: �׳��쳣Code
  repMsg: �쳣��Ϣ
  
  ```
  
  | �ֶ�| ����| ˵�� |
  | :-----| :---- | :---- |
  | repCode |string | �׳��쳣Code �� 1001: �����쳣��1202: appKeyΪ�գ�1203: appKeyУ��ʧ�ܣ�1207: ϵͳ�汾�Ų���Ϊ�գ�1208: Ӧ�ð汾�Ų���Ϊ�� |
  | repMsg |string | �쳣��Ϣ |