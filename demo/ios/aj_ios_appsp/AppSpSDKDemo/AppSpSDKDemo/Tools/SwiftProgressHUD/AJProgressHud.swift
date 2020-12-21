//
//  AJProgressHud.swift
//  AJCVehicle
//
//  Created by Black on 2018/6/11.
//  Copyright © 2018年 WuXingLin. All rights reserved.
//

import UIKit

class AJProgressHud: NSObject {
    
    //statusbar 文案提示
    class func showTopStatusBarText(_ text: String) {
        SwiftProgressHUD.showOnNavigation(text,
                                          autoClear: true,
                                          autoClearTime: 1,
                                          textColor: UIColor.white,
                                          fontSize: 14,
                                          backgroundColor: UIColor(red:0, green:0, blue:0, alpha: 0.6))
    }
    
    // loading 加重动画，锁定页面，等待加载结果 需要手动隐藏
    class func showMessage(_ message: String? = nil) {
        if let msg = message {
            SwiftProgressHUD.showWaitLoading(msg)
        } else {
            SwiftProgressHUD.showWait()
        }
    }
    
    //显示提示文字，默认显示 1.5s 自动消失
    class func showText(_ text: String, _ interval: TimeInterval = 1.5) {
        SwiftProgressHUD.showOnlyText(text, interval)
    }
    
    //隐藏
    class func hidHud() {
        delay(0) {
            SwiftProgressHUD.hideAllHUD()
        }
    }
}
