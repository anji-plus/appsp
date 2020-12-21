//
//  AppSpDeviceInfo.swift
//  AppSpSDK
//
//  Created by Black on 2020/9/8.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit
import SystemConfiguration

class AppSpDeviceInfo: NSObject {
    //初始化设备信息接口
    class func deviceInit() {
        AppSpRequest.share.request(path: AppSpDeviceInitPath, success: { (repData) in
        }) { (errorData) in
        }
    }
    //直接返回接口请求的基础参数字典
    class func getDeviceInfo() -> [String: String] {
        return  [
           "brand": getBrandInfo(),
           "deviceId": getDeviceId(),
           "sdkVersion": AppSpSDKVersion,
            "netWorkStatus": AppSpService.shareService.connectionStatus,
           "osVersion": getOSVerison(),
           "platform": getPlatform(),
           "screenInfo": getScreenInfo(),
           "versionCode": getAppBuildCode(),
           "versionName": getAppVersion()
         ]
    }
}

extension AppSpDeviceInfo {
    //获取版本名
    class func getAppVersion() -> String {
        let infoDictionary = Bundle.main.infoDictionary
        let majorVersion = infoDictionary?["CFBundleShortVersionString"] as! String//主程序版本号
        return majorVersion
    }
    //获取版本号
    class func getAppBuildCode() -> String {
        let infoDictionary = Bundle.main.infoDictionary
        let appBuild = infoDictionary?["CFBundleVersion"] as! String//主程序版本号
        return appBuild
    }
    //获取屏幕宽高
    class func getScreenInfo() -> String {
        let width = UIScreen.main.bounds.size.width
        let height = UIScreen.main.bounds.size.height
        return "\(height)*\(width)"
    }
    //获取系统版本
    class func getOSVerison() -> String {
        return UIDevice.current.systemVersion
    }
    //获取系统平台
    class func getPlatform() -> String {
        return UIDevice.current.systemName
    }
    //获取UUID
    class func getDeviceId() -> String {
        let identifierNumber = UIDevice.current.identifierForVendor
        return identifierNumber?.uuidString ?? ""
    }
    //获取手机品牌信息
    class func getBrandInfo() -> String {
        return UIDevice().iphoneTypeName
    }
    
}

extension UIDevice {
    
    var iphoneTypeName: String {
        var systemInfo = utsname()
        uname(&systemInfo)
        
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        let platform = machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8, value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
        
        switch platform {
            
        case "iPhone3,1", "iPhone3,2", "iPhone3,3":  return "iPhone 4"
        case "iPhone4,1":                             return "iPhone 4s"
        case "iPhone5,1", "iPhone5,2":                 return "iPhone 5"
        case "iPhone5,3", "iPhone5,4":                 return "iPhone 5c"
        case "iPhone6,1", "iPhone6,2":                 return "iPhone 5s"
        case "iPhone7,2":                                return "iPhone 6"
        case "iPhone7,1":                                return "iPhone 6 Plus"
        case "iPhone8,1":                                return "iPhone 6s"
        case "iPhone8,2":                                return "iPhone 6s Plus"
        case "iPhone8,4":                                return "iPhone SE"
        case "iPhone9,1":                                return "iPhone 7 (CDMA)"
        case "iPhone9,2":                                return "iPhone 7 Plus (CDMA)"
        case "iPhone9,3":                                return "iPhone 7 (GSM)"
        case "iPhone9,4":                                return "iPhone 7 Plus (GSM)"
        case "iPhone10,1", "iPhone10,4":               return "iPhone 8"
        case "iPhone10,2", "iPhone10,5":               return "iPhone 8 Plus"
        case "iPhone10,3", "iPhone10,6":               return "iPhone X"
        case "iPhone11,2":                               return "iPhone XS "
        case "iPhone11,4", "iPhone11,6":               return "iPhone XS Max"
        case "iPhone11,8":                              return "iPhone XR"
        case "iPhone12,1":                              return "iPhone 11"
        case "iPhone12,3":                              return "iPhone 11 Pro"
        case "iPhone12,5":                              return "iPhone 11 Pro Max"
        case "iPhone13,1":                              return "iPhone 12 mini"
        case "iPhone13,2":                              return "iPhone 12"
        case "iPhone13,3":                              return "iPhone 12  Pro"
        case "iPhone13,4":                              return "iPhone 12  Pro Max"

        case "iPad2,1", "iPad2,2", "iPad2,3", "iPad2,4":return "iPad 2"
        case "iPad3,1", "iPad3,2", "iPad3,3":           return "iPad 3"
        case "iPad3,4", "iPad3,5", "iPad3,6":           return "iPad 4"
        case "iPad4,1", "iPad4,2", "iPad4,3":           return "iPad Air"
        case "iPad5,3", "iPad5,4":                      return "iPad Air 2"
        case "iPad2,5", "iPad2,6", "iPad2,7":           return "iPad Mini"
        case "iPad4,4", "iPad4,5", "iPad4,6":           return "iPad Mini 2"
        case "iPad4,7", "iPad4,8", "iPad4,9":           return "iPad Mini 3"
        case "iPad5,1", "iPad5,2":                      return "iPad Mini 4"
        case "iPad6,3", "iPad6,4":                      return "iPad Pro 9.7"
        case "iPad6,7", "iPad6,8":                      return "iPad Pro 12.9"
            
        case "AppleTV5,3":                              return "Apple TV 4"
            
        case "iPod5,1":                                  return "iPod Touch 5"
        case "iPod7,1":                                  return "iPod Touch 6"
            
        case "i386", "x86_64":                          return "Simulator"
            
        default:                                        return platform
        }
    }
}

