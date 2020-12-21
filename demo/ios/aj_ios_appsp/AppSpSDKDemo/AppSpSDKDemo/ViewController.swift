//
//  ViewController.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/18.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit
import AppSpSDK

let TableCellKey = "TableCellKey"

class ViewController: UIViewController {
    
    let tableView = UITableView()
    var actionList = [[String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "AppSp SDK Demo"
        //初始化AppSpService
        
//        AppSpService.shareService.setAppkey(appKey: "your App Key")
        /**
         * AppSpService.shareService.initConfig 该接口SDK 0.0.3版本支持 增加如下功能
         * appkey: 移动服务平台创建应用获取
         * debug： 是否显示调试log 默认开启
         * host: 配置自己的host 服务地址 默认nil
         */
        AppSpService.shareService.initConfig(appkey: "your App Key", debug: true)
//        AppSpService.shareService.initConfig(appkey: <#T##String#>, debug: <#T##Bool#>, <#T##host: String?##String?#>)
        
        configureView()
        configureData()
        
    }
    
    func configureView() {
        tableView.frame = self.view.bounds
        tableView.delegate = self
        tableView.dataSource = self
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: TableCellKey)
        self.view.addSubview(tableView)
    }
    
    func configureData() {
        actionList.removeAll()
        actionList.append(["版本更新", "checkVersion"])
        actionList.append(["公告信息", "getNotcie"])
    }
    //检查版本更新
    @objc func checkVersion() {
        let controller = VersionController()
        self.navigationController?.pushViewController(controller, animated: true)
    }
    //获取公告信息
    @objc func getNotcie() {
        let controller = NoticeController()
        self.navigationController?.pushViewController(controller, animated: true)
    }
}

extension ViewController: UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return actionList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: TableCellKey, for: indexPath)
        let actions = actionList[indexPath.row]
        cell.textLabel?.text = actions[0]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let actions = actionList[indexPath.row]
        let actionName = actions[1]
        let sel: Selector = NSSelectorFromString(actionName)
        self.perform(sel)
     }
    
}

