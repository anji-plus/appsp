
# 快速接入使用
## 初始化服务
AppKey的获取见 **操作手册**

```java
public class AppContext extends Application {
    private static AppContext mInstance;
    //appkey是在Appsp创建应用生成的
    public static final String appKey = "aadcfae6215a4e0f9bf5bc5edccb1045";

    public static AppContext getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
		//关键代码
        AppSpConfig.getInstance()
                .init(this, appKey)
                //可修改基础请求地址
                .setHost("http://open-appsp.anji-plus.com/sp/")
                //正式环境可以禁止日志输出，通过Tag APP-SP过滤看日志
                .setDebuggable(BuildConfig.DEBUG ? true : false)
                //务必要初始化，否则后面请求会报错
                .deviceInit();
    }

}
```

## 版本更新服务
* 权限

```java
    <!-- apk存储 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- 网络请求 -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- apk升级 -->
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <!-- 7.0+文件读取 com.anji.appsp.sdktest是Demo的包名-->
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

* file_paths.xml代码

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

* 调用

 ```java
 private void checkVersion() {
        AppSpConfig.getInstance().setVersionUpdateCallback(new IAppSpVersionUpdateCallback() {
            @Override
            public void update(AppSpModel<AppSpVersion> spModel) {
                //注意，当前是子线程
                //成功处理 TODO
            }

            @Override
            public void error(String code, String msg) {
               //注意，当前是子线程
			   //失败处理 TODO
            }
        });
    }
 ```
 
**请求返回数据结构如下**
 
**spModel** 数据详情
 
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
 
***
 
 | 字段| 类型| 说明 |
 | :-----| :---- | :---- |
 | repCode |String | 业务返回码，0000表示成功  |
 | repMsg |String | 业务日志、异常信息 |
 | repData | Object | 请求业务数据包、详情见下 |
 
**repData** 数据详情
 
 | 字段| 类型| 说明 |
 | :-----| :---- | :---- |
 | downloadUrl |String | app下载地址 |
 | mustUpdate | boolean | 是否强制更新，true为强制更新; false为非强制更新 |
 | showUpdate | boolean | 是否提示更新：允许弹出更新 |
 | updateLog |String | 更新日志 |
 
***

**errorInfo** 数据详情
 
 ```java
 repCode: 抛出异常Code
 repMsg: 异常信息
 
 ```
 
 | 字段| 类型| 说明 |
 | :-----| :---- | :---- |
 | repCode |String | 抛出异常Code ； 1001: 请求异常；1202: appKey为空；1203: appKey校验失败；1207: 系统版本号不能为空；1208: 应用版本号不能为空 |
 | repMsg |String | 异常信息 |
 
 ### 公告服务
   ```java
    private void checkNotice() {
        AppSpConfig.getInstance().getNotice(new IAppSpNoticeCallback() {
            @Override
            public void notice(AppSpModel<List<AppSpNoticeModelItem>> spModel) {
                //注意，当前是子线程
                //成功处理 TODO
            }

            @Override
            public void error(String code, String msg) {
                //注意，当前是子线程
                //失败处理 TODO
            }
        });

    }
   ```
**请求返回数据结构如下**

***
**spModel** 数据详情

```java
 {
 	"repCode": "0000", //业务返回码，0000表示成功
 	"repMsg": "成功", //业务日志
 	"repData": [ // 返回数据为 List
 		{
	 		"title": "公告标题",
	 		"details": "公告内容",
	 		"templateType": "dialog", //公告类型（ 弹窗：dialog； 水平滚动：horizontal_scroll）
	 		"templateTypeName": "公告"//公告模板名称
 		}
 	]
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

**errorInfo** 数据详情
  
  ```java
  repCode: 抛出异常Code
  repMsg: 异常信息
  
  ```
  
  | 字段| 类型| 说明 |
  | :-----| :---- | :---- |
  | repCode |string | 抛出异常Code ； 1001: 请求异常；1202: appKey为空；1203: appKey校验失败；1207: 系统版本号不能为空；1208: 应用版本号不能为空 |
  | repMsg |string | 异常信息 |