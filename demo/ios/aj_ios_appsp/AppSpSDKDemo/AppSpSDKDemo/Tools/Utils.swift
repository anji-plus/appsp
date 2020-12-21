//
//  Utils.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/11.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

/// 延时主线程执行
func delay(_ seconds: Double = 2, closure: @escaping () -> ()) {
    let _t = DispatchTime.now() + Double(Int64(Double(NSEC_PER_SEC) * seconds)) / Double(NSEC_PER_SEC)
    DispatchQueue.main.asyncAfter(deadline: _t, execute: closure)
}

func createCAGradientLayer(_ startColor: UIColor, _ endColor: UIColor, _ frame: CGRect) -> CAGradientLayer {
    let layer = CAGradientLayer()
    layer.frame = frame
    layer.startPoint = CGPoint(x: 0, y: 0.5)
    layer.endPoint = CGPoint(x: 1, y: 0.5)
    layer.colors = [endColor.cgColor, startColor.cgColor]
    layer.locations = [0, 1]
    return layer
}

func createGradientLayerImage(layer: CALayer) -> UIImage? {
    UIGraphicsBeginImageContextWithOptions(layer.frame.size, false, 0)
    layer.render(in: UIGraphicsGetCurrentContext()!)
    let image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image
}

func myMAX(_ x: CGFloat, _ y: CGFloat) -> CGFloat {
    return x > y ? x : y
}

