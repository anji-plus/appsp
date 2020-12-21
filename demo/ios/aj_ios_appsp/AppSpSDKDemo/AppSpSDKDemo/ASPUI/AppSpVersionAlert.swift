//
//  AppSpVersionAlert.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/10.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

class AppSpVersionAlert: AppSpAlert {
    private var mustUpdate: Bool = false
    //强制更新
    func showMustUpdateAlert(content: String,
                             confirmHandler: @escaping ((_ alert: AppSpAlert)->())) {
        showAction(isMustUpdate: true,
                   title: "哇，发现新版本啦！",
                   content: content,
                   confirmTitle: "马上更新",
                   cancelTitle: "",
                   confirmHandler: confirmHandler,
                   cancelHandler: nil)
    }
    //非强制更新
    func showUpdateAlert(content: String,
                         confirmHandler: @escaping ((_ alert: AppSpAlert)->()),
                         cancelHandler: @escaping ((_ alert: AppSpAlert)->())) {
        showAction(isMustUpdate: false,
                   title: "哇，发现新版本啦！",
                   content: content,
                   confirmTitle: "马上更新",
                   cancelTitle: "取消",
                   confirmHandler: confirmHandler,
                   cancelHandler: cancelHandler)
    }
    //基础弹窗
    private func showAction(isMustUpdate: Bool = false,
                        title: String? = nil,
                        content: String? = nil,
                        confirmTitle: String = "是",
                        cancelTitle: String = "否",
                        confirmHandler: ((_ alert: AppSpAlert)->())? = nil,
                        cancelHandler: ((_ alert: AppSpAlert)->())? = nil) {
        self.title = title ?? ""
        self.content = content ?? ""
        self.confirmTitle = confirmTitle
        self.cancelTitle = cancelTitle
        self.confirmHandler = confirmHandler
        self.cancelHandler = cancelHandler
        self.mustUpdate = isMustUpdate
        setCommonUI()
    }
    
    private func setCommonUI() {
        //背景图
        let bgView = UIView()
        bgView.backgroundColor = UIColor.white
        bgView.layer.cornerRadius = 16
        bgView.layer.masksToBounds = true
        self.addSubview(bgView)
        
        bgView.snp.makeConstraints { (make) in
            make.width.equalTo(285)
            make.centerX.equalTo(self)
            make.centerY.equalTo(self)
        }
        
        // 删除按键
        let deleteButton = UIButton()
        deleteButton.addTarget(self, action: #selector(cancelClick), for: .touchUpInside)
        deleteButton.setImage(UIImage(named: "icon_delete"), for: .normal)
        bgView.addSubview(deleteButton)
        deleteButton.snp.makeConstraints { (make) in
            make.right.equalTo(-5)
            make.top.equalTo(5)
            make.width.equalTo(30)
            make.height.equalTo(30)
        }
        deleteButton.isHidden = mustUpdate
        
        //顶部视图
        let headImgV = UIImageView()// 200 100
        headImgV.image = UIImage(named: "version")
        headImgV.contentMode = .scaleToFill
        self.addSubview(headImgV)
        headImgV.snp.makeConstraints { (make) in
            make.centerX.equalTo(bgView)
            make.bottom.equalTo(bgView.snp.top).offset(80)
            make.width.equalTo(230)
            make.height.equalTo(115)
        }
        
        //设置title 设置content
        let titleLabel = UILabel()
        titleLabel.text = self.title
        titleLabel.textAlignment = .center
        titleLabel.font = UIFont.systemFont(ofSize: 17)
        bgView.addSubview(titleLabel)
        
        titleLabel.snp.makeConstraints { (make) in
            make.left.equalTo(20)
            make.top.equalTo(80)
            make.right.equalTo(-20)
            make.height.equalTo(20)
        }
        //详细内容
        let contentTextView = UITextView()
        contentTextView.text = self.content
        contentTextView.font = UIFont.systemFont(ofSize: 15)
        contentTextView.backgroundColor = UIColor.white
        contentTextView.textColor = UIColor(red: 58/255.0, green: 58/255.0, blue: 58/255.0, alpha: 1.0)
        contentTextView.textAlignment = .left
        contentTextView.isEditable = false
        bgView.addSubview(contentTextView)
        
        contentTextView.snp.makeConstraints { (make) in
            make.left.equalTo(20)
            make.top.equalTo(titleLabel.snp.bottom)
            make.right.equalTo(-20)
            make.height.equalTo(80)
            make.bottom.equalTo(bgView).offset(-25)
        }
        
        //底部更新视图
        let button = UIButton()
        button.addTarget(self, action: #selector(confirmClick), for: .touchUpInside)
        button.layer.cornerRadius = 20
        button.layer.masksToBounds = true
        button.setTitleColor(UIColor.white, for: .normal)
        button.titleLabel?.font = UIFont.systemFont(ofSize: 15)
        let layerFrame = CGRect(x: 0, y: 0, width: 160, height: 40)
        let startColor = UIColor("6067FC")
        let endColor = UIColor("A67EFD")
        let layer = createCAGradientLayer(startColor, endColor, layerFrame)
        let layerImg = createGradientLayerImage(layer: layer)
        button.setBackgroundImage(layerImg, for: .normal)
        button.setTitle(confirmTitle, for: .normal)
        self.addSubview(button)
        
        button.snp.makeConstraints { (make) in
            make.top.equalTo(bgView.snp.bottom).offset(-20)
            make.width.equalTo(160)
            make.height.equalTo(40)
            make.centerX.equalTo(bgView)
        }
        
        show()
    }
    
    @objc func confirmClick() {
        if self.confirmHandler != nil {
            self.confirmHandler!(self)
        }
        self.dismiss()
    }
    
    @objc func cancelClick() {
        if self.cancelHandler != nil {
            self.cancelHandler!(self)
        }
        self.dismiss()
    }
    

}
