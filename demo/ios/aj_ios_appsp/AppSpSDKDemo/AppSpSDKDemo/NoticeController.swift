//
//  NoticeController.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/10.
//  Copyright © 2020 Black. All rights reserved.
//
import UIKit
import AppSpSDK

fileprivate let NoticeTitle_test = "公告信息"
fileprivate let NoticeContent_test = "   经销商伙伴你好，受今年第9号台风“美莎克”影响，天津、辽宁、吉林、黑龙江等地车辆运输周期将有所延误。"
//公告类型
let NoticeType_dialog = "dialog"
let NoticeType_scroll = "horizontal_scroll"

class NoticeController: UIViewController {
    
    private let actionArray = ["公告API", "弹出公告UI", "滚动公告UI"]
    private let Button_W:CGFloat = 200
    private let Button_H:CGFloat = 45
    private let Margin_Y:CGFloat = 30
    private let Margin_X:CGFloat = (ASP_SCREENW - 200)*0.5
    
    private var noticeContentArr: [String] = [String]()
    //公告UI
    private let noticeView = UIView()
    private let marqueeView = AppSpMarqueeView()

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "公告信息"
        self.view.backgroundColor = UIColor.white
        
        for (index, element) in actionArray.enumerated(){
            let button = createButton()
            button.addTarget(self, action: #selector(buttonClick(_:)), for: .touchUpInside)
            button.setTitle(element, for: .normal)
            button.frame = CGRect(x: Margin_X, y: Margin_Y + (Margin_Y + Button_H)*CGFloat(index), width: Button_W, height: Button_H)
            self.view.addSubview(button)
        }
        
        createNoticeView()
    }
    
    @objc func buttonClick(_ btn: UIButton) {
        if let btnTitle = btn.titleLabel?.text{
            //公告请求
            if btnTitle == actionArray[0] {
                getNoticeApi()
            } else if btnTitle == actionArray[1] { //弹窗公告
                showAlertNoticeUI(title: NoticeTitle_test, content: NoticeContent_test)
            } else { //滚动公告UI
                showScrollNoticeUI(contentArray: [NoticeContent_test])
            }
        }
    }
    //获取版本更新
    private func getNoticeApi() {
        AJProgressHud.showMessage("公告请求中...")
        AppSpService.shareService.getNoticeInfo(success: { [weak self] (result) in
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
        let nowTime: Int =  Int(Date().timeIntervalSince1970*1000)
        let code: String = result["repCode"] as? String ?? ""
        if code == "0000" {
            if let repData = result["repData"] as? [[String: Any]] {
                if repData.count == 0 {
                    delay(0.3) {
                       AJProgressHud.showText("无匹配公告数据")
                    }
                    return
                }
                noticeContentArr.removeAll()
                //匹配公告时间
                for (_, item) in repData.enumerated() {
                    let title: String = item["title"] as? String ?? ""
                    let details: String = item["details"] as? String ?? ""
                    let templateType: String = item["templateType"] as? String ?? ""
                    let startTime: Int = item["startTime"] as? Int ?? 0
                    let endTime: Int = item["endTime"] as? Int ?? 0
                    if nowTime >= startTime && nowTime < endTime {
                        if templateType == NoticeType_dialog {
                            showAlertNoticeUI(title: title, content: details)
                        } else {
                            noticeContentArr.append(details)
                        }
                    }
                }
                if noticeContentArr.count > 0 {
                    showScrollNoticeUI(contentArray: noticeContentArr)
                }
            } else {
                delay(0.3) {
                   AJProgressHud.showText("\(result["repCode"] ?? ""):\(result["repMsg"] ?? "")")
                }
            }
        }
    }
    
    //alert 弹窗样式
    private func showAlertNoticeUI(title: String? = nil, content: String? = nil) {
        let showTitle = title ?? NoticeTitle_test
        let showContent = content ?? ""
        DispatchQueue.main.async {
            AppSpNoticeAlert().showNoticeAlert(title: showTitle, content: showContent) { (alert) in
                print("我知道了")
            }
        }
    }
    //scroll 滚动样式
    private func showScrollNoticeUI(contentArray: [String]) {
        DispatchQueue.main.async {
            self.noticeView.isHidden = false
            self.marqueeView.backgroundColor = UIColor.white
            self.marqueeView.textList = contentArray
            self.marqueeView.run()
        }
    }
    
    private func createNoticeView() {
        let Notice_Y: CGFloat = (Button_H + Margin_Y)*4
        let Notice_X: CGFloat = 20
        let Notice_W: CGFloat = (ASP_SCREENW - Notice_X*2)
        let Notice_H: CGFloat = 30
        
        noticeView.frame = CGRect(x: Notice_X, y: Notice_Y, width: Notice_W, height: Notice_H)
        let noticeIV = UIImageView()
        noticeIV.image = UIImage(named: "notice")
        noticeIV.frame = CGRect(x: 0, y: 0, width: Notice_H, height: Notice_H)
        noticeView.addSubview(noticeIV)
        
        marqueeView.frame = CGRect(x: noticeIV.frame.maxX + 10, y: 5, width: Notice_W - noticeIV.frame.maxX - 20, height: Notice_H - 10)
        noticeView.addSubview(marqueeView)
        self.view.addSubview(noticeView)
        noticeView.isHidden = true
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
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidAppear(animated)
        marqueeView.stop()
    }
}
