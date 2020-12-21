Pod::Spec.new do |s|
    s.name         = 'AJSDKAppSp'
    s.version      = '0.0.3'
    s.summary      = 'ios AJSDKAppSp versionUpdate Notice'
    s.homepage     = 'https://github.com/anji-plus/aj_ios_appsp'
    s.license      = 'MIT'
    s.authors      = {'blackxu' => '747373635@qq.com'}
    s.platform     = :ios, '9.0'
    s.source       = {:git => 'https://github.com/anji-plus/aj_ios_appsp.git', :tag => s.version}
    s.requires_arc = true
    s.swift_version = '4.0'
    s.source_files = 'AppSpSDK/AppSpSDK/**/*.swift'
end
