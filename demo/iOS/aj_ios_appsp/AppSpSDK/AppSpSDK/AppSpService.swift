//
//  AppSpService.swift
//  AppSpSDK
//
//  Created by Black on 2020/9/8.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit
// test appKey: 64cf5a851f37c6c0ab7a3186a2377d5d

public class AppSpService: NSObject {
    @objc public static let shareService = AppSpService()
    fileprivate var _appKey: String?
    fileprivate let reachability = try! AJAppSpReachability()
    var connectionStatus:String = "WIFI"
    
    private(set) var isDebug : Bool = true //是否显示log 默认
    private(set) var appSpBaseURL = "https://appsp.anji-plus.com"
    
    deinit {
        reachability.stopNotifier()
    }
    public override init() {
        super.init();
        
        reachability.whenReachable = { [weak self] reachability in
            if reachability.connection == .wifi {
                self?.connectionStatus = "WIFI"
            } else {
                self?.connectionStatus = "4G"
            }
        }
        reachability.whenUnreachable = { [weak self] _ in
            self?.connectionStatus = ""
        }

        do {
            try reachability.startNotifier()
        } catch {
        }
    }
    
    //初始化使用服务 后续会删除该方，建议使用如下 func initConfig() 该方法进行初始化
    @objc public func setAppkey(appKey: String) {
        _appKey = appKey
        deviceInit()
    }
    /**
     * initConfig 该接口SDK 0.0.3版本支持 增加如下功能；
     * appkey: 移动服务平台创建应用获取
     * debug： 是否显示调试log 默认开启
     * host: 配置自己的host 服务地址 默认nil
     */
    @objc public func initConfig(appkey: String, debug: Bool = true, _ host: String? = nil) {
        isDebug = debug
        if host != nil {
            appSpBaseURL = host!
        }
        self.setAppkey(appKey: appkey)
        
    }
    
    //获取appkey 用于请求接口
    func getAppKey() -> String {
        return _appKey ?? ""
    }
    //获取版本更新接口
    @objc public func checkVersionUpdate(success: @escaping (_ response: [String : Any]) -> (),
                                   failure: @escaping ((_ errorInfo: [String: Any]) -> ())) {
        AppSpVersion.checkVersionUpdate(success: success, failure: failure)
    }
    //获取公告信息接口
    @objc public func getNoticeInfo(success: @escaping (_ response: [String : Any]) -> (),
                              failure: @escaping ((_ errorInfo: [String: Any]) -> ())) {
        AppSpNotice.getNotice(success: success, failure: failure)
    }
    //初始化设备信息
    private func deviceInit() {
        AppSpDeviceInfo.deviceInit()
    }
    
}


