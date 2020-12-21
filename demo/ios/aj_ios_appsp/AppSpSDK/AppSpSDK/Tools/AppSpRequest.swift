//
//  AppSpRequest.swift
//  AppSpSDK
//
//  Created by Black on 2020/9/9.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

//请求超时
private let requestTimeOut: TimeInterval = 10
//响应超时
private let responseTimeOut: TimeInterval = 30
//自定义错误码
private let CustomErrorCode = "604"

class AppSpRequest: NSObject {
    static let share = AppSpRequest()
    var _appKey: String?
    var _apiSession: URLSession!
    //初始化请求session
    override init() {
        super.init()
        let configuration = URLSessionConfiguration.default
        configuration.httpAdditionalHeaders = ["Accept": "application/json"]
        configuration.timeoutIntervalForRequest = requestTimeOut
        configuration.timeoutIntervalForResource = responseTimeOut
        _apiSession = URLSession(configuration: configuration)
    }
    
    //项目暂时仅支持POST请求 GET后续再写
    func request(path: String,
                 options: [String: Any]? = nil,
                 success: @escaping (_ response: [String : Any]) -> (),
                 failure: @escaping ((_ errorInfo: [String: Any]) -> ())) {
        
        let reqUrlStr = AppSpService.shareService.appSpBaseURL + path
        
        guard let reqUrl = URL(string: reqUrlStr) else {
            failure(self.apiErrorInfo(code: CustomErrorCode, messge: "URL解析异常"))
            return
        }
        var request = URLRequest(url: reqUrl)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        //获取appKey
        let appKey = AppSpService.shareService.getAppKey()
        //获取设备信息
        let deviceParams = AppSpDeviceInfo.getDeviceInfo()
        //拼装请求使用数据
        var paramData: [String: Any] = ["appKey": appKey]
        for (key, value) in deviceParams {
            paramData[key] = value
        }
        
        var params: [String: Any] = [
            "data": paramData,
            "sign": ""
        ]
        let oriEnStr = self.toEncryptString(data: paramData)
        if oriEnStr != nil {
           params["sign"]! = AppSpRSACrypt.encrypt(oriEnStr!, AJ_RSA_PUBLIC_KEY_TAG) ?? ""
        }
        appSpLog("========================================")
        appSpLog("reqUrl: \(reqUrl)")
        appSpLog("params: \(params)")
        
        request.httpBody = formateRequestBody(parmas: params)
            
        _apiSession.dataTask(with: request) { (data, response, error) in
            if (error != nil) {
                appSpLog(error)
                appSpLog("========================================")
                if let err = error as NSError? {
                    failure(self.apiErrorInfo(code: "\(err.code)", messge: err.localizedDescription))
                } else {
                    failure(self.apiErrorInfo(code: CustomErrorCode, messge: "请求异常"))
                }
                return
            }
            
            guard let repData = data else {
                failure(self.apiErrorInfo(code: CustomErrorCode, messge: "服务器数据异常"))
                return
            }
            
            var dict:[String: Any]?
            do {
                dict = try JSONSerialization.jsonObject(with: repData,
                                                        options: JSONSerialization.ReadingOptions.init(rawValue: 0)) as? Dictionary
            } catch {
                failure(self.apiErrorInfo(code: CustomErrorCode, messge: "数据解析异常"))
                return
            }
            
            if let repDict = dict {
                let repMsg = repDict["repMsg"] as? String
                if let repCode = repDict["repCode"] as? String {
                    if repCode == "0000" {
                        success(repDict)
                    } else {
                        failure(self.apiErrorInfo(code: repCode, messge: repMsg ?? "数据异常"))
                    }
                }
                appSpLog("response: \(repDict)")
            } else {
                failure(self.apiErrorInfo(code: CustomErrorCode, messge: "数据解析异常"))
            }
            appSpLog("========================================")
        }.resume()
    }
    
    
}
extension AppSpRequest {
    //序列化 POST 请求Body数据
    func formateRequestBody(parmas: [String: Any]) -> Data? {
        return try? JSONSerialization.data(withJSONObject: parmas,
                                           options: .prettyPrinted)
    }
    
    //封装自定义error info
    func apiErrorInfo(code: String, messge: String) -> [String: Any]{
        return [
            "repCode": code,
            "repMsg": messge
        ]
    }
    //加密前 把字典对象转换成需要加密的字符串
    func toEncryptString(data: [String: Any]) -> String? {
        do {
            let newData = try JSONSerialization.data(withJSONObject: data,
                                                     options: .fragmentsAllowed)
            return String(data: newData as Data, encoding: .utf8)
        }catch{
            return nil
        }
    }
    //解密后字符串转换成字典
    func toDecryptDict(encryptedStr: String) -> [String: Any]? {
        guard let jsonObject = encryptedStr.data(using: .utf8) else { return nil}
        do {
            return try JSONSerialization.jsonObject(with: jsonObject,
            options: JSONSerialization.ReadingOptions.init(rawValue: 0)) as? [String : Any]
        } catch {
            return nil
        }
    }
}
