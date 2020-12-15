### 版本更新的配置请参考 android集成/iOS集成

# Flutter集成 - 版本更新
### host配置

```
针对Windows环境，C:\Windows\System32\drivers\etc 的host文件加入

199.232.4.133 raw.githubusercontent.com

针对Mac环境，在/etc/hosts下加入
```

### 在工程/pubspec.yaml中，加入依赖：

```
aj_flutter_appsp:
    git:
      url: https://github.com/anji-plus/aj_flutter_appsp.git
      ref: ^0.0.1

```
其中ref表示版本名，对应仓库的tag号
^表示用最新版本，如果指定版本，请忽略此符号

### 获取插件，终端输入

```
flutter packages get
```

或者点击右上角Packages get

### 引用

```
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
```

### 调用（详细使用请参考插件的example）

```
    //举例，调用时请替换掉此appKey
   String appKey = "24b14615101b4fe0ab9595d6e1d5e428";
   SpRespUpdateModel updateModel =
        await AjFlutterAppSp.getUpdateModel(appKey: appKey);
```

其中SpRespUpdateModel包括字段有
```
    //     apk下载/跳转的url
    public String url;
    //      如果是true，表示外部url，被认为需要跳转到网页进行下载，比如跳转到应用宝下载
	//      否则直接在应用内部下载，下载的方式开发者可自行定义，也可参考我们提供的Demo
    public boolean isExternalUrl;
    //      是否需要弹出更新提示，true表示需要
    public boolean showUpdate;
    //      是否需要强制更新，true表示需要强更，此时需要阻断用户操作，如果不更新则退出APP
    public boolean mustUpdate;
    //      状态码，文件下载过程中可能出现异常，状态码分为
	//      AppSpStatusCode.StatusCode_Success ： 成功
	//      AppSpStatusCode.StatusCode_Cancel  ： 用户已取消json文件下载
	//      AppSpStatusCode.StatusCode_Timeout ： 服务器json文件地址连接超时
	//      AppSpStatusCode.StatusCode_UrlFormatError ： 请求地址格式错误
    public int statusCode;
    //      更新日志
    public String updateLog;
```

# 区别-Android 集成
### android目录下，AndroidManifest.xml中加入权限，其中包括网络访问、文件访问、apk安装的权限

```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

### android目录下，AndroidManifest.xml中继续添加provider，方便Android7.0+版本升级


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


其中，file_paths.xml若没有则创建，在xml目录下，file_paths.xml内容如下：


```
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path
        name="files_root"
        path="Android/data/包名/" />
    <external-path
        name="external_storage_root"
        path="." />
    <root-path
        name="root_path"
        path="" />

</paths>
```

# Flutter集成 - 公告
### 引用

```
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
```

### 调用（详细使用请参考插件的example）
```
    //举例，调用时请替换掉此appKey
    String appKey = "24b14615101b4fe0ab9595d6e1d5e428";
    SpRespNoticeModel noticeModel =
        await AjFlutterAppSp.getNoticeModel(appKey: appKey);
```
### 插件下载
若要参考具体集成流程，可参考我们提供的插件，
插件下载地址：

[https://github.com/anji-plus/aj_flutter_appsp](https://github.com/anji-plus/aj_flutter_appsp)


如有集成问题，请加我们微信群交流：

![appsp微信群](https://upload-images.jianshu.io/upload_images/1801706-bfc97af5b0d036a3.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/320)
