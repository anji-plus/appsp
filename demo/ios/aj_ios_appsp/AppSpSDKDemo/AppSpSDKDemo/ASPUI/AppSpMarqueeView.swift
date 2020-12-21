//
//  AppSpMarqueeView.swift
//  AppSpSDKDemo
//
//  Created by Black on 2020/9/28.
//  Copyright © 2020 Black. All rights reserved.
//

import UIKit

class AppSpMarqueeView: UIView {
    
    // 跑马灯的滚动状态
    enum MarqueeState {
        case running
        case paused
        case stopped
    }
    
    var isRunning: Bool { return state == .running }
    var isPaused: Bool { return state == .paused }
    var isStopped: Bool { return state == .stopped }
    
    fileprivate(set) var state: MarqueeState = .stopped
    
    /// 每条news之间的间距，只能在stopped状态下设置
    @IBInspectable var textSpacing: CGFloat = 100 {
        willSet {
            precondition(state == .stopped)
        }
    }
    
    /// newsBar的滚动速度，默认 50 pt/s,只能在stopped状态下设置
    @IBInspectable var textScrollSpeed: CGFloat = 50 {
        willSet {
            precondition(state == .stopped)
        }
    }
    
    /// news文本颜色，默认black
    @IBInspectable var textColor: UIColor = .black {
        willSet {
            adjustTextLabelColor()
        }
    }
    
    /// 文本字体，默认15号，只能在stopped状态下设置
    @IBInspectable var textFont: UIFont = UIFont.systemFont(ofSize: 15) {
        didSet {
            precondition(state == .stopped)
            adjustTextLabelFont()
        }
    }
    
    /// 文本列表
    var textList = [String]() {
        willSet { stop() }
        didSet { resetIndex() }
    }
    
    /// 跑马灯前后的模糊渐变视图，默认为15
    @IBInspectable var fadeWidth: CGFloat = 15 {
        didSet {
            layoutGradientMasks()
            setGradientMaskHidden(!(isGradientMaskColorVisible() && fadeWidth > 0))
        }
    }
    
    /// newsBar的背景色
    override var backgroundColor: UIColor? {
        didSet {
            adjustGradientMaskColor()
            adjustContentBackgroundColor()
        }
    }
    
    fileprivate var nextIndex = NSNotFound
    fileprivate var displayLink: CADisplayLink?
    fileprivate let textLabelContainerView = UIView()
    fileprivate var onScreenTextLabels =  [TextLabel]()
    fileprivate var offScreenTextLabels =  [TextLabel]()
    
    fileprivate let leftGradientMask = CAGradientLayer()
    fileprivate let rightGradientMask = CAGradientLayer()
    
    deinit {
        invalidateDisplayLink()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}

fileprivate extension AppSpMarqueeView {
    class TextLabel: UILabel {}
}

// MARK: - 滚动控制
extension AppSpMarqueeView {
    
    // 开始滚动
    func run() {
        guard !isRunning else { return }
        guard !textList.isEmpty else { return }
        
        if state == .stopped {
            addOnScreenTextLabel()
        }
        
        if window != nil {
            resumeDisplayLink()
        }
        state = .running
    }
    
    // 暂停滚动
    func pause() {
        guard isRunning else { return }
        pauseDisplayLink()
        state = .paused
    }
    
    // 停止滚动
    func stop() {
        guard !isPaused else { return }
        resetIndex()
        pauseDisplayLink()
        clearOnScreenTextLabels()
        resetContainerViewBounds()
        state = .stopped
    }
}

// 初始化
fileprivate extension AppSpMarqueeView {
    
    func commonInit() {
        textLabelContainerView.clipsToBounds = true
        addSubview(textLabelContainerView)
        
        leftGradientMask.startPoint = CGPoint(x: 0, y: 0.5)
        leftGradientMask.endPoint = CGPoint(x: 1, y: 0.5)
        layer.addSublayer(leftGradientMask)
        
        rightGradientMask.startPoint = CGPoint(x: 0, y: 0.5)
        rightGradientMask.endPoint = CGPoint(x: 1, y: 0.5)
        layer.addSublayer(rightGradientMask)
        
        adjustGradientMaskColor()
    }
    
}

// MARK: - 布局
extension AppSpMarqueeView {
    
    override var bounds: CGRect {
        didSet {
            guard bounds != oldValue else { return }
            layoutSubviews2()
        }
    }
    
    override var frame: CGRect {
        didSet {
            guard frame != oldValue else { return }
            layoutSubviews2()
        }
    }
    
    fileprivate func layoutSubviews2() {
        layoutContainerView()
        layoutGradientMasks()
        layoutOnScreenTextLabels()
    }
    
    fileprivate func layoutContainerView() {
        textLabelContainerView.frame = bounds
    }
    
    fileprivate func layoutGradientMasks() {
        leftGradientMask.frame = CGRect(x: 0, y: 0, width: fadeWidth, height: bounds.height)
        rightGradientMask.frame = CGRect(x: bounds.maxX - fadeWidth, y: 0, width: fadeWidth, height: bounds.height)
    }
    
    fileprivate func layoutOnScreenTextLabels() {
        onScreenTextLabels.forEach { $0.center.y = bounds.midY }
    }
    
    fileprivate func resetContainerViewBounds() {
        textLabelContainerView.bounds.origin = .zero
    }
}

// MARK: - 字体
fileprivate extension AppSpMarqueeView {
    
    func adjustTextLabelFont() {
        onScreenTextLabels.forEach { $0.font = textFont }
        offScreenTextLabels.forEach { $0.font = textFont }
    }
}

// MARK: - 颜色
fileprivate extension AppSpMarqueeView {
    
    func adjustGradientMaskColor() {
        guard let startColor = backgroundColor?.cgColor, startColor.alpha > 0 else {
            setGradientMaskHidden(true)
            return
        }
        
        let endColor = startColor.copy(alpha: 0)!
        
        leftGradientMask.isHidden = fadeWidth <= 0
        leftGradientMask.colors = [startColor, endColor]
        
        rightGradientMask.isHidden = fadeWidth <= 0
        rightGradientMask.colors = [endColor, startColor]
    }
    
    func adjustTextLabelColor() {
        onScreenTextLabels.forEach { $0.textColor = textColor }
        offScreenTextLabels.forEach { $0.textColor = textColor }
    }
    
    func adjustContentBackgroundColor() {
        textLabelContainerView.backgroundColor = backgroundColor
        
        let clipsToBounds: Bool = {
            if let color = backgroundColor?.cgColor, color.alpha > 0 {
                return true
            }
            return false
        }()
        
        onScreenTextLabels.forEach {
            $0.clipsToBounds = clipsToBounds
            $0.backgroundColor = backgroundColor
        }
        
        offScreenTextLabels.forEach {
            $0.clipsToBounds = clipsToBounds
            $0.backgroundColor = backgroundColor
        }
    }
    
    func isGradientMaskColorVisible() -> Bool {
        if let color = backgroundColor?.cgColor, color.alpha > 0 {
            return true
        }
        return false
    }
}

// MARK: - 渐变遮罩
fileprivate extension AppSpMarqueeView {
    
    func setGradientMaskHidden(_ hidden: Bool) {
        leftGradientMask.isHidden = hidden
        rightGradientMask.isHidden = hidden
    }
}

// MARK: - 更新索引
fileprivate extension AppSpMarqueeView {
    
    func increaseIndex() {
        nextIndex = (nextIndex + 1) % textList.count
    }
    
    func resetIndex() {
        nextIndex = textList.isEmpty ? NSNotFound : 0
    }
}

// MARK: - 循环利用
fileprivate extension AppSpMarqueeView {
    
    func dequeueReusableTextLabel() -> TextLabel {
        if let textLabel = offScreenTextLabels.popLast() {
            return textLabel
        }
        
        let textLabel = TextLabel()
        textLabel.font = textFont
        textLabel.textColor = textColor
        textLabel.backgroundColor = backgroundColor
        
        if let color = backgroundColor?.cgColor, color.alpha > 0 {
            textLabel.clipsToBounds = true
        } else {
            textLabel.clipsToBounds = false
        }
        
        return textLabel
    }
    
    func recycle(_ textLabel: TextLabel) {
        offScreenTextLabels.append(textLabel)
    }
}

// MARK: - 添加移除标签
fileprivate extension AppSpMarqueeView {
    
    func addOnScreenTextLabel() {
        let currentIndex = nextIndex
        increaseIndex()
        
        let textLabel = dequeueReusableTextLabel()
        textLabel.text = textList[currentIndex]
        textLabel.sizeToFit()
        textLabel.center.y = frame.height * 0.5
        if let lastLabelFrame = onScreenTextLabels.last?.frame {
            textLabel.frame.origin.x = lastLabelFrame.maxX + textSpacing
        } else {
            textLabel.frame.origin.x = textLabelContainerView.bounds.maxX
        }
        
        onScreenTextLabels.append(textLabel)
        textLabelContainerView.addSubview(textLabel)
    }
    
    func clearOnScreenTextLabels() {
        onScreenTextLabels.forEach {
            $0.removeFromSuperview()
            recycle($0)
        }
        onScreenTextLabels.removeAll(keepingCapacity: true)
    }
    
    func removeOffScreenTextLabel() {
        let textLabel = onScreenTextLabels.removeFirst()
        textLabel.removeFromSuperview()
        recycle(textLabel)
    }
}

// MARK: - 定时器
fileprivate extension AppSpMarqueeView {
    
    func invalidateDisplayLink() {
        displayLink?.invalidate()
        displayLink = nil
    }
    
    func resumeDisplayLink() {
        if displayLink == nil {
            let target = DisplayLinkTarget(owner: self)
            let displayLink = CADisplayLink(target: target, selector: #selector(DisplayLinkTarget.step))
            displayLink.add(to: .main, forMode: .common)
            self.displayLink = displayLink
        }
        displayLink?.isPaused = false
    }
    
    func pauseDisplayLink() {
        displayLink?.isPaused = true
    }
    
    class DisplayLinkTarget {
        
        weak var owner: AppSpMarqueeView?
        
        init(owner: AppSpMarqueeView) {
            self.owner = owner
        }
        
        @objc func step(_ displayLink: CADisplayLink) {
            guard let marqueeLabel = owner else { return }
            
            let originXOffset = marqueeLabel.textScrollSpeed * CGFloat(displayLink.duration)
            marqueeLabel.textLabelContainerView.bounds.origin.x += originXOffset
            
            if let firstLabel = marqueeLabel.onScreenTextLabels.first,
                firstLabel.frame.maxX <= marqueeLabel.textLabelContainerView.bounds.minX
            {
                marqueeLabel.removeOffScreenTextLabel()
            }
            
            if let lastLabelMaxX = marqueeLabel.onScreenTextLabels.last?.frame.maxX,
                marqueeLabel.textLabelContainerView.bounds.maxX - lastLabelMaxX >= marqueeLabel.textSpacing
            {
                marqueeLabel.addOnScreenTextLabel()
            }
        }
    }
}

// MARK: - 视图关系变更
extension AppSpMarqueeView {
    
    override func willMove(toWindow newWindow: UIWindow?) {
        if newWindow == nil {
            pauseDisplayLink()
        } else if isRunning {
            resumeDisplayLink()
        }
    }
}
