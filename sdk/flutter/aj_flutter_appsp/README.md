# Flutter手册
## 简介
 本文档主要介绍加加移动服务平台Flutter 插件的开发和集成，适用于有一定Flutter基础的开发人员。
* 插件开发思路
Android/iOS开发好原生SDK后发布到远程仓库github/maven/jcenter,插件原生部分依赖SDK，如此精简代码，便于维护
想看SDK实现思路，可查看Android SDK
* SDK文件结构
```
lib
├── aj_flutter_appsp.dart          // 定义MethodChannel和与原生交互的方法，比如初始化、获取版本信息
├── aj_flutter_appsp_lib.dart      // dart文件统一管理，归整为library aj_flutter_appsp;
├── sp_notice_model_item.dart      // 每条公告的信息
├── sp_resp_notice_model.dart      // 公告数据
├── sp_resp_update_model.dart      // 版本数据

```

* 支持版本说明
> * Flutter SDK >= 1.5.4
> * Android Studio 3.0+ / Xcode 11+
> * JDK 1.8 / Swift 5.0+

## 插件集成
### host配置

```
针对Windows环境，C:\Windows\System32\drivers\etc 的host文件加入

199.232.4.133 raw.githubusercontent.com

针对Mac环境，在/etc/hosts下加入
```

### 依赖

在工程/pubspec.yaml中，加入依赖：

```
aj_flutter_appsp:
    git:
      url: https://github.com/anji-plus/aj_flutter_appsp.git
      ref: branch_0.0.1        //可选，用名为branch_0.0.1的branch

```
其中ref表示版本名，对应仓库的branch，不用则用主分支
^表示用最新版本，如果指定版本，请忽略此符号

### 获取插件

终端输入

```
flutter packages get
```

或者点击右上角Packages get

### 引用

```java
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
```

### 调用

详细使用请参考插件的example

* 初始化
在main.dart加入

```java
    @override
  void initState() {
    super.initState();
    _initAppSp();
  }

  _initAppSp() async {
    //初始化
    var debuggable = !bool.fromEnvironment("dart.vm.product");
    await AjFlutterAppSp.init(
		//appKey，创建应用时生成的，作为和服务端通信的标识
        appKey: "aadcfae6215a4e0f9bf5bc5edccb1045",
		//请求的基础地址
        host: "http://open-appsp.anji-plus.com/sp/",
		//是否打开debug开关，非生产默认打开
        debug: debuggable);
  }

```

* 版本信息获取
```java
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';

  _update() async {
	//关键代码
    SpRespUpdateModel updateModel =
        await AjFlutterAppSp.getUpdateModel();
	if (!mounted) {
      return;
    }
    if (updateModel == null) {
      Scaffold.of(context).showSnackBar(
        SnackBar(content: Text("没有更新信息")),
      );
      return;
    }
  }	
```

* 公告信息获取
```java
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';

  _requestNoticeType() async {
    //无需改造数据，用服务器返回数据，下面的都是模拟的数据
    //ignore
    SpRespNoticeModel noticeModel = await AjFlutterAppSp.getNoticeModel();
    if (!mounted) {
      return;
    }
    if (noticeModel == null ||
        noticeModel.repData == null ||
        noticeModel.repData.isEmpty) {
      var snackBar = SnackBar(content: Text("没有公告信息"));
      _scaffoldkey.currentState.showSnackBar(snackBar);
      return;
    }
```

* 版本更新Android配置

关于版本更新的弹出和下载更新，需要注意：

1， android目录下，AndroidManifest.xml中加入权限，其中包括网络访问、文件访问、apk安装的权限

```java
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

2， android目录下，AndroidManifest.xml中添加provider，方便Android7.0+版本升级

```java
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
`authorities`的格式是 包名.fileprovider

其中，file_paths.xml若没有则创建，在xml目录下，file_paths.xml内容如下：

```java
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

我们可以在`aj_flutter_appsp.dart`加入apk安装的逻辑，然后在插件处理，
这里我是直接放在了工程的`MainActivity`，插件只关心数据的获取，
业务逻辑由开发自行定制，example仅作参考，不作为行业标准

关于版本更新的弹出和下载更新，以及公告的展示，请参考我们的Flutter插件，附有Example

## 插件实现
### 版本更新

* aj_flutter_appsp.dart实现

```java
import 'aj_flutter_appsp_lib.dart';

import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';

 ///获取版本信息
  static Future<SpRespUpdateModel> getUpdateModel() async {
    final String jsonStr = await _channel.invokeMethod('getUpdateModel');
    SpRespUpdateModel updateModel =
    SpRespUpdateModel.fromJson(json.decode(jsonStr));
    return updateModel;
  }
```
**请求返回数据结构如下**

***
**SpRespUpdateModel** 数据详情

```java
 {
 	"repCode": "0000", //业务返回码，0000表示成功
 	"repMsg": "成功",  //业务日志
 	"repData": {
 		"downloadUrl": "app下载地址",
 		"mustUpdate": false, //是否强制更新，true为强制更新
 		"showUpdate": true, //是否允许弹出更新
 		"updateLog": "更新日志"
 	}
 }
```

| 字段| 类型| 说明 |
| :-----| :---- | :---- |
| repCode |String | 业务返回码，0000表示成功  |
| repMsg |String | 业务日志、异常信息 |
| repData |Object | 请求业务数据包、详情见下 |

**repData** 数据详情

| 字段| 类型| 说明 |
| :-----| :---- | :---- |
| downloadUrl |String | app下载地址 |
| mustUpdate | boolean | 是否强制更新，true为强制更新; false为非强制更新 |
| showUpdate | boolean | 是否提示更新：允许弹出更新 |
| updateLog |String | 更新日志 |

***

### 获取公告

* aj_flutter_appsp.dart实现

```java
import 'aj_flutter_appsp_lib.dart';

import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';

  ///获取公告信息
  static Future<SpRespNoticeModel> getNoticeModel() async {
    final String jsonStr = await _channel.invokeMethod('getNoticeModel');
    SpRespNoticeModel noticeModel =
    SpRespNoticeModel.fromJson(json.decode(jsonStr));
    return noticeModel;
  }
```
**请求返回数据结构如下**

***
**SpRespNoticeModel** 数据详情

```java
 {
 	"repCode": "0000", //业务返回码，0000表示成功
 	"repMsg": "成功",  //业务日志
 	"repData": {
	 		"title": "公告标题",
	 		"details": "公告内容",
	 		"templateType": "dialog", //公告类型（ 弹窗：dialog； 水平滚动：horizontal_scroll）
	 		"templateTypeName": "公告"//公告模板名称
 		}
 }
```

| 字段| 类型| 说明 |
| :-----| :---- | :---- |
| repCode |String | 业务返回码，0000表示成功  |
| repMsg |String | 业务日志、异常信息 |
| repData |Object | 请求业务数据包、详情见下 |

**repData** 数据详情

| 字段| 类型| 说明 |
| :-----| :---- | :---- |
| title |String | 公告标题 |
| details |String | 公告内容 |
| templateType |String | 公告类型（ 弹窗：dialog； 水平滚动：horizontal_scroll）|
| templateTypeName |String | 公告模板名称 |

***

### Android Plugin实现

在AjFlutterAppspPlugin.java

```java
	//为了解决并发问题，比如多次点击，异常情况时候容易出问题
    private ConcurrentLinkedQueue<MethodResultWrapper> wrappers = new ConcurrentLinkedQueue<>();
	
	@Override
    public void onMethodCall(MethodCall call, Result result) {
        removeAllWrapper();
        addWraper(new MethodResultWrapper(result));
        //初始化
        if (call.method.equals("init")) {
            String appKey = null;
            String host = null;
            boolean debug = true;
            Object parameter = call.arguments();
            if (parameter instanceof Map) {
                appKey = (String) ((Map) parameter).get("appKey");
                host = (String) ((Map) parameter).get("host");
                debug = (Boolean) ((Map) parameter).get("debug");
                init(appKey, host, debug);
            }
            //版本请求
        } else if (call.method.equals("getUpdateModel")) {
            checkVersion();
            //公告获取
        } else if (call.method.equals("getNoticeModel")) {
            checkNotice();
        } else {
            MethodResultWrapper wrapper = peekWraper();
            if (wrapper != null) {
                wrapper.notImplemented();
            }
        }
    }
	
	/**
     * 获取当前的result
     *
     * @return
     */
    private MethodResultWrapper peekWraper() {
        if (wrappers == null
                || wrappers.isEmpty()) {
            return null;
        }
        return wrappers.remove();
    }

    /**
     * 只考虑最后一次
     */
    private void removeAllWrapper() {
        if (wrappers == null) {
            return;
        }
        wrappers.clear();
    }

    /**
     * 加入唯一的result
     *
     * @param wrapper
     */
    private void addWraper(MethodResultWrapper wrapper) {
        if (wrappers == null) {
            return;
        }
        wrappers.add(wrapper);
    }

```

* 初始化

```java
	/**
     * 初始化
     *
     * @param appKey 创建应用时生成的，作为和服务端通信的标识
     * @param host   如果为空，认为用SDK默认请求地址
     * @param debug  日志开关是否打开，默认打开
     */
    private void init(String appKey, String host, boolean debug) {
        AppSpConfig.getInstance()
                .init(registrar.activity(), appKey)
                //可修改基础请求地址
                .setHost(host)
                //正式环境可以禁止日志输出，通过Tag APP-SP过滤看日志
                .setDebuggable(debug)
                //务必要初始化，否则后面请求会报错
                .deviceInit();
        MethodResultWrapper wrapper = peekWraper();
        if (wrapper != null) {
            wrapper.success("");
        }
    }
```

* 版本信息获取

```java
	/**
     * 版本更新检查
     */
    private void checkVersion() {
        AppSpConfig.getInstance().getVersion(new IAppSpVersionCallback() {
            @Override
            public void update(AppSpModel<AppSpVersion> spModel) {
                AppSpLog.d("Test updateModel is " + spModel);
                MethodResultWrapper wrapper = peekWraper();
                if (spModel == null) {
                    if (wrapper != null) {
                        wrapper.notImplemented();
                    }
                } else {
                    //先转成json
                    if (spModel.getRepData() != null) {//有更新数据
                        if (wrapper != null) {
                            wrapper.success(new Gson().toJson(spModel));
                        }
                    } else {//无更新数据
                        AppSpModel tempModel = new AppSpModel<>();
                        tempModel.setRepCode(spModel.getRepCode());
                        tempModel.setRepMsg(spModel.getRepMsg());
                        if (wrapper != null) {
                            wrapper.success(new Gson().toJson(tempModel));
                        }
                    }
                }

            }

            @Override
            public void error(String code, String msg) {
				//无更新数据
                MethodResultWrapper wrapper = peekWraper();
                AppSpModel spModel = new AppSpModel<>();
                spModel.setRepCode(code);
                spModel.setRepMsg(msg);
                if (wrapper != null) {
                    wrapper.success(new Gson().toJson(spModel));
                }
            }
        });
    }
```

* 公告信息获取

```java
	/**
	 * 公告信息获取
	 */
	private void checkNotice() {
        AppSpConfig.getInstance().getNotice(new IAppSpNoticeCallback() {
            @Override
            public void notice(AppSpModel<List<AppSpNoticeModelItem>> noticeModel) {
                AppSpLog.d("Test noticeModel is " + noticeModel);
                MethodResultWrapper wrapper = peekWraper();
                if (noticeModel == null) {
                    if (wrapper != null) {
                        wrapper.notImplemented();
                    }
                } else if (noticeModel.getRepData() != null) {//有公告信息
                    if (wrapper != null) {
                        wrapper.success(new Gson().toJson(noticeModel));
                    }
                } else {//无公告信息
                    //先转成json
                    AppSpModel tempModel = new AppSpModel<>();
                    tempModel.setRepCode(noticeModel.getRepCode());
                    tempModel.setRepMsg(noticeModel.getRepMsg());
                    if (wrapper != null) {
                        wrapper.success(new Gson().toJson(tempModel));
                    }
                }
            }

            @Override
            public void error(String code, String msg) {
				//无公告信息
                AppSpModel noticeModel = new AppSpModel<>();
                noticeModel.setRepCode(code);
                noticeModel.setRepMsg(msg);
                MethodResultWrapper wrapper = peekWraper();
                if (wrapper != null) {
                    wrapper.success(new Gson().toJson(noticeModel));
                }
            }
        });
    }
```


### iOS Plugin实现

## Flutter插件下载
地址: [https://gitee.com/anji-plus/aj-appsp/flutter](https://gitee.com/anji-plus/aj-appsp/flutter)
