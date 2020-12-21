# iOS SDK集成
### 1.使用CocoaPods接入SDK
* 使用CocoaPods引入 **AJSDKAppSp** 先进入项目Podfile文件进行配置

```
platform :ios, '9.0'
use_frameworks!

target '<Your Target Name>' do
    # 示例：0.0.1，可以选择指定发布版本
    pod 'AJSDKAppSp', '~> 0.0.1' 
end

```
* 然后打开终端，进入Podfile所在目录路径

```
$ pod install
```
* 支持版本说明

> * iOS 9.0+ 
> * Xcode 11+ 
> * Swift 5.0+

### 2.快速接入如下使用
**1、初始化服务**

```
import UIKit
//导入头文件
import AJSDKAppSp

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.lightGray
        
        //初始化SDK服务
        initAJSDKConfigure()
        
    }
    
    //初始化SDK数据 (建议在项目启动以后 AppDelegate 中初始化)
    func initAJSDKConfigure() {
        let appKey = "移动管理平台获取自己的AppKey"
        AppSpService.shareService.setAppkey(appKey: appKey)
    }
}
```
**2、获取版本信息**

```
  /*
     获取 版本更新数据
     1、因为是异步，操作UI建议判断是否是主线程
     2、repInfo：服务器响应数据包（具体格式见下文）
     3、errorInfo：请求异常error信息（具体格式见下文）
     */
    func requestUpdateVersion() {
        AppSpService.shareService.checkVersionUpdate { (repInfo) in
            print(repInfo)
        } failure: { (errorInfo) in
            print(errorInfo)
        }
    }
```
**请求返回数据结构如下**
***
repInfo

```
repCode: 业务返回码，0000表示成功
repMsg: 业务日志
repData: 返回数据

    repData 数据格式为
        downloadUrl: 下载apk地址或者市场下载地址
        updateLog: 更新日志
        showUpdate: 是否允许弹出更新
        mustUpdate: 是否强制更新，true为强制更新

 {
     "repCode": "0000",
     "repMsg": "成功",
     "repData": {
         "downloadUrl": "app下载地址",
         "mustUpdate": false,
         "showUpdate": true,
         "updateLog": "更新日志"
     },
     "success": true,
     "error": false
 }
```
***
errorInfo

```
//repCode: 抛出异常Code
//repMsg: 异常信息

```

**3、获取公告信息**

```
  /*
        获取 公告信息数据
     1、因为是异步，操作UI建议判断是否是主线程
     2、repInfo：服务器响应数据包（具体格式见下文）
     3、errorInfo：请求异常error信息（具体格式见下文）
     */
    func requestNoticeInfo() {
        AppSpService.shareService.getNoticeInfo { (repInfo) in
            print(repInfo)
        } failure: { (errorInfo) in
            print(errorInfo)
        }

    }
```
**请求返回数据结构如下**
***
repInfo

```
repCode: 业务返回码，0000表示成功
repMsg: 业务日志
repData: 返回数据为 Array

repData 数据格式为[]
    title: 公告标题
    details: 公告内容
    templateType: 公告类型（ 弹窗：dialog； 水平滚动：horizontal_scroll）
    startTime: 公告有效开始时间（毫秒级）
    endTime: 公告有效截止时间（毫秒级）

 {
     "repCode": "0000",
     "repMsg": "成功",
     "repData": [
         {
             "title": "公告标题",
             "details": "公告内容",
             "templateType": "dialog",
             "startTime": 1601186454000,
             "endTime": 1601359255000
         }
     ],
     "success": true,
     "error": false
 }
```
***
errorInfo

```
//repCode: 抛出异常Code
//repMsg: 异常信息

```
