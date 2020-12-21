//
//  VersionController.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/10.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit
import AppSpSDK

fileprivate let UpdateMsg_test = "发现新版本，请耐心更新，谢谢"
fileprivate let DefaultAppStoreLink = "itms-apps://ax.itunes.apple.com/"

class VersionController: UIViewController {
    
    private let actionArray = ["版本更新API", "普通更新UI", "强制更新UI"]
    private let Button_W:CGFloat = 200
    private let Button_H:CGFloat = 45
    private let Margin_Y:CGFloat = 30
    private let Margin_X:CGFloat = (ASP_SCREENW - 200)*0.5

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "版本更新"
        self.view.backgroundColor = UIColor.white
        
        for (index, element) in actionArray.enumerated(){
            let button = createButton()
            button.addTarget(self, action: #selector(buttonClick(_:)), for: .touchUpInside)
            button.setTitle(element, for: .normal)
            button.frame = CGRect(x: Margin_X, y: Margin_Y + (Margin_Y + Button_H)*CGFloat(index), width: Button_W, height: Button_H)
            self.view.addSubview(button)
        }
    }
    
    @objc func buttonClick(_ btn: UIButton) {
        if let btnTitle = btn.titleLabel?.text{
            //版本更新请求
            if btnTitle == actionArray[0] {
                getUpdateApi()
            } else if btnTitle == actionArray[1] { //普通更新UI
                updateVersionUI()
            } else { //强制更新UI
                mustUpdateVersionUI()
            }
        }
    }
    //获取版本更新
    private func getUpdateApi() {
        AJProgressHud.showMessage("版本更新请求中...")
        AppSpService.shareService.checkVersionUpdate(success: { [weak self] (result) in
            AJProgressHud.hidHud()
            self?.hanlerRepData(result)
        }) { (errorInfo) in
            AJProgressHud.hidHud()
            print("errorInfo: \(errorInfo)")
            delay(0.3) {
                AJProgressHud.showText("\(errorInfo["repCode"] ?? ""):\(errorInfo["repMsg"] ?? "")")
            }
            
        }
    }
    
    private func hanlerRepData(_ result: [String: Any]) {
        print("repData: \(result)")
        let code: String = result["repCode"] as? String ?? ""
        if code == "0000" {
            if let repData = result["repData"] as? [String: Any] {
                let showUpdate: Bool = repData["showUpdate"] as? Bool ?? false
                let mustUpdate: Bool = repData["mustUpdate"] as? Bool ?? false
                let appstoreLink: String = repData["downloadUrl"] as? String ?? ""
                let updateLog: String = repData["updateLog"] as? String ?? ""
                if showUpdate && mustUpdate {
                    mustUpdateVersionUI(appstoreLink, updateLog)
                } else if showUpdate {
                    updateVersionUI(appstoreLink, updateLog)
                }
            } else {
                delay(0.3) {
                   AJProgressHud.showText("\(result["repCode"] ?? ""):\(result["repMsg"] ?? "")")
                }
            }
        }
    }
    //普通更新UI样式
    private func updateVersionUI(_ link: String = "", _ msg: String = "") {
        var showMsg = UpdateMsg_test
        if msg.count > 0 {
            showMsg = msg
        }
        DispatchQueue.main.async {
            AppSpVersionAlert().showUpdateAlert(content: showMsg, confirmHandler: { (alert) in
                print("更新")
                let appLink: String = (link.count > 0) ? link : DefaultAppStoreLink
                UIApplication.shared.openURL(URL(string: appLink)!)
            }) { (alert) in
                print("取消")
            }
        }
    }
    //强制更新UI样式
    private func mustUpdateVersionUI(_ link: String = "", _ msg: String = "") {
        var showMsg = UpdateMsg_test
        if msg.count > 0 {
            showMsg = msg
        }
        DispatchQueue.main.async {
            AppSpVersionAlert().showMustUpdateAlert(content: showMsg) { (alert) in
                print("立即更新")
                let appLink: String = (link.count > 0) ? link : DefaultAppStoreLink
                UIApplication.shared.openURL(URL(string: appLink)!)
                delay {
                    exit(0)
                }
            }
        }
    }
    
    
    private func createButton() -> UIButton {
        //确认点击button
        let btnColor = UIColor(red: 95/255.0, green: 144/255.0, blue: 232/255.0, alpha: 1.0)
        let button = UIButton()
        button.layer.cornerRadius = 20
        button.layer.masksToBounds = true
        button.setTitleColor(UIColor.white, for: .normal)
        button.titleLabel?.font = UIFont.systemFont(ofSize: 15)
        button.backgroundColor = btnColor
        return button
    }


}
