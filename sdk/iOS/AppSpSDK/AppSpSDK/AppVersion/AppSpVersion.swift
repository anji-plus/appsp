//
//  AppSpVersion.swift
//  AppSpSDK
//
//  Created by Black on 2020/9/8.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

class AppSpVersion: NSObject {
    class func checkVersionUpdate(success: @escaping (_ response: [String : Any]) -> (),
                                  failure: @escaping ((_ errorInfo: [String: Any]) -> ())) {
        AppSpRequest.share.request(path: AppSpAppVersionPath,
                                   success: success,
                                   failure: failure)
    }
}

