//
//  AppSpVersionAlert.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/10.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

/// 屏幕的宽
let ASP_SCREENW = UIScreen.main.bounds.size.width
/// 屏幕的高
let ASP_SCREENH = UIScreen.main.bounds.size.height

class AppSpAlert: UIView {
    
    var confirmHandler:((_ alert: AppSpAlert)->())?
    var cancelHandler:((_ alert: AppSpAlert)->())?
    
    var title: String = ""
    var content: String = ""
    
    var confirmTitle: String = "是"
    var cancelTitle: String = "否"
    
    init() {
        super.init(frame: CGRect(x: 0, y: 0, width: ASP_SCREENW, height: ASP_SCREENH))
        self.backgroundColor = UIColor.black.withAlphaComponent(0.5)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // show
    func show() {
        delay(0) {
            UIView.animate(withDuration: 0.25) {
                UIApplication.shared.keyWindow?.addSubview(self)
            }
        }
    }
    // 销毁
    func dismiss() {
        delay(0) {
            UIView.animate(withDuration: 0.25) {
                self.removeFromSuperview()
            }
        }
    }
}

extension UIView {
    public func playSpringAnimation() {
        self.transform = CGAffineTransform(scaleX: 0.8, y: 0.8)
        UIView.animate(withDuration: 0.9, delay: 0.0, usingSpringWithDamping: 0.3, initialSpringVelocity: 0.9, options: .curveEaseOut, animations: {
            self.transform = CGAffineTransform(scaleX: 1.0, y: 1.0)
        }, completion: nil)
    }
}
