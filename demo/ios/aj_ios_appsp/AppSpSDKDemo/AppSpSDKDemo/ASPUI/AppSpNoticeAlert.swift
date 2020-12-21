//
//  AppSpNoticeAlert.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/10.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit
import SnapKit

class AppSpNoticeAlert: AppSpAlert {
    
    func showNoticeAlert(title: String,
                         content: String,
                         confirmHandler: @escaping ((_ alert: AppSpAlert)->())) {
        self.title = title
        self.content = content
        self.confirmHandler = confirmHandler
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
        
        //标题
        let titleLabel = UILabel()
        titleLabel.text = self.title
        titleLabel.textAlignment = .center
        titleLabel.font = UIFont.boldSystemFont(ofSize: 17)
        bgView.addSubview(titleLabel)
        
        titleLabel.snp.makeConstraints { (make) in
            make.left.equalTo(20)
            make.top.equalTo(20)
            make.right.equalTo(-20)
            make.height.equalTo(20)
        }
        // 删除按键
        let deleteButton = UIButton()
        deleteButton.addTarget(self, action: #selector(deleteClick), for: .touchUpInside)
        deleteButton.setImage(UIImage(named: "icon_delete"), for: .normal)
        bgView.addSubview(deleteButton)
        deleteButton.snp.makeConstraints { (make) in
            make.right.equalTo(-5)
            make.top.equalTo(5)
            make.width.equalTo(30)
            make.height.equalTo(30)
        }
        //内容
        let contentTextView = UITextView()
        contentTextView.text = self.content
        contentTextView.font = UIFont.systemFont(ofSize: 15)
        contentTextView.backgroundColor = UIColor.white
        contentTextView.textColor = UIColor(red: 58/255.0, green: 58/255.0, blue: 58/255.0, alpha: 1.0)
        contentTextView.textAlignment = .left
        contentTextView.isEditable = false
        bgView.addSubview(contentTextView)
        
        contentTextView.snp.makeConstraints { (make) in
            make.left.equalTo(titleLabel.snp.left)
            make.top.equalTo(titleLabel.snp.bottom).offset(10)
            make.right.equalTo(titleLabel.snp.right)
            make.height.equalTo(90)
        }
        
        //确认点击button
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
        button.setTitle("我知道了", for: .normal)
        bgView.addSubview(button)
        
        
        button.snp.makeConstraints { (make) in
            make.top.equalTo(contentTextView.snp.bottom).offset(10)
            make.width.equalTo(160)
            make.height.equalTo(40)
            make.centerX.equalTo(bgView)
            make.bottom.equalTo(bgView).offset(-15)
        }
        
        self.show()
        
    }
    
    @objc func confirmClick() {
        if confirmHandler != nil {
            confirmHandler!(self)
        }
        self.dismiss()
    }
    
    @objc func deleteClick() {
        self.dismiss()
    }

}
