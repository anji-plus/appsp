# aj_flutter_appsp
# Flutter���� - �汾����
# AppSp �������ַ�ǣ�http://open-appsp.anji-plus.com
### host����

```
���Windows������C:\Windows\System32\drivers\etc ��host�ļ�����

199.232.4.133 raw.githubusercontent.com

���Mac��������/etc/hosts�¼���
```

### �ڹ���/pubspec.yaml�У�����������

```
aj_flutter_appsp:
    git:
      url: https://github.com/anji-plus/aj_flutter_appsp.git
      ref: 0.0.2

```
����ref��ʾ�汾������Ӧ�ֿ��tag��,Ҳ������^0.0.1��
^��ʾ�����°汾�����ָ���汾������Դ˷���

### ��ȡ������ն�����

```
flutter packages get
```

���ߵ�����Ͻ�Packages get

### ����

```
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
```

### ���ã���ϸʹ����ο������example��
#### ��main.dart�У���ʼ��
```

 @override
  void initState() {
    super.initState();
    _initAppSp();
  }

  _initAppSp() async {
    //��ʼ��,appKey��host����Ӧ����Ҫ����
    var debuggable = !bool.fromEnvironment("dart.vm.product");
    await AjFlutterAppSp.init(
        appKey: "aadcfae6215a4e0f9bf5bc5edccb1045",
        host: "http://open-appsp.anji-plus.com/sp/",
        debug: debuggable);
  }
  
```

#### �ڰ汾����ҳ��

```
    //�汾����
   SpRespUpdateModel updateModel =
        await AjFlutterAppSp.getUpdateModel();
```

����SpRespUpdateModel�����ֶ���
```
    //     apk����/��ת��ҳ��url�������.apk����Ϊ�����أ�������ת��ҳ
    public String downloadUrl;
   
    //      �Ƿ���Ҫ����������ʾ��true��ʾ��Ҫ
    public boolean showUpdate;
    //      �Ƿ���Ҫǿ�Ƹ��£�true��ʾ��Ҫǿ������ʱ��Ҫ����û�������������������˳�APP
    public boolean mustUpdate;
    //      ������־
    public String updateLog;
```

# ����-Android ����
### androidĿ¼�£�AndroidManifest.xml�м���Ȩ�ޣ����а���������ʡ��ļ����ʡ�apk��װ��Ȩ��

```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

### androidĿ¼�£�AndroidManifest.xml�м������provider������Android7.0+�汾����


```
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


���У�file_paths.xml��û���򴴽�����xmlĿ¼�£�file_paths.xml�������£�


```
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path
        name="files_root"
        path="Android/data/����/" />
    <external-path
        name="external_storage_root"
        path="." />
    <root-path
        name="root_path"
        path="" />

</paths>
```

# Flutter���� - ����
### ����


```
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
```

### ���ã���ϸʹ����ο������example��
```
    //����
    
    SpRespNoticeModel noticeModel =
        await AjFlutterAppSp.getNoticeModel();
```
### �������
��Ҫ�ο����弯�����̣��ɲο������ṩ�Ĳ����
������ص�ַ��

[https://github.com/anji-plus/aj_flutter_appsp](https://github.com/anji-plus/aj_flutter_appsp)


���м������⣬�������΢��Ⱥ������

![appsp΢��Ⱥ](https://upload-images.jianshu.io/upload_images/1801706-bfc97af5b0d036a3.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/320)


