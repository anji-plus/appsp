//
//  AppDelegate.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/18.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.makeKeyAndVisible()
        
        let VC = ViewController()
        VC.view.backgroundColor = UIColor.white
        let Nav = UINavigationController(rootViewController: VC)
        Nav.navigationBar.isTranslucent = false
        self.window?.rootViewController = Nav
        
        return true
    }


}

