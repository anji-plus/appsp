//
//  AppSpUtils.swift
//  AppSpSDK
//
//  Created by Black on 2020/9/9.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

///
/// - Parameters:
///   - message: 输出内容
///   - file: 输出文件位置
///   - method: 对应方法
///   - line: 所在行
func appSpLog<T>(_ message: T,
                 file: String = #file,
                 method: String = #function,
                 line: Int = #line)
{
    #if DEBUG
        if AppSpService.shareService.isDebug {
            print("[AppSpSDK][\(appSpGetCurrentTime())][Line \(line)] \((file as NSString).lastPathComponent)\(method): \(message)")
        }
    #endif
}

func appSpGetCurrentTime() -> String{
    let formatter = DateFormatter()
    formatter.dateFormat = "HH:mm:ss:SSS"
    let timerString: String = formatter.string(from: Date())
    return timerString
}
