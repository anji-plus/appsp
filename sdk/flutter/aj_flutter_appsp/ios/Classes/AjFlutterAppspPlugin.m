#import "AjFlutterAppspPlugin.h"
#import <AJSDKAppSp/AJSDKAppSp-Swift.h>
@implementation AjFlutterAppspPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"aj_flutter_appsp"
            binaryMessenger:[registrar messenger]];
  AjFlutterAppspPlugin* instance = [[AjFlutterAppspPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
//    版本更新
  if ([call.method isEqualToString: @"getUpdateModel"]) {
      NSString *appKey = call.arguments[@"appKey"];
      [[AppSpService shareService] setAppkeyWithAppKey:appKey];
      __weak typeof(self) weakSelf = self;
      [[AppSpService shareService] checkVersionUpdateWithSuccess:^(NSDictionary* repData) {
          result([weakSelf formateDictToJSonString:repData]);
      } failure:^(NSDictionary* errorData) {
          result([weakSelf formateDictToJSonString:errorData]);
      }];
  } else if ([call.method isEqualToString:@"getNoticeModel"]) {//通知栏
      NSString *appKey = call.arguments[@"appKey"];
      [[AppSpService shareService] setAppkeyWithAppKey:appKey];
      __weak typeof(self) weakSelf = self;
      [[AppSpService shareService] getNoticeInfoWithSuccess:^(NSDictionary* repData) {
          result([weakSelf formateDictToJSonString:repData]);
      } failure:^(NSDictionary* errorData) {
          result([weakSelf formateDictToJSonString:errorData]);
      }];
      
  } else {
      result(FlutterMethodNotImplemented);
  }
}

//字典转jsonstring
- (NSString *)formateDictToJSonString:(NSDictionary*) dict {
    NSError * error = nil;
    NSData * jsonData = [NSJSONSerialization dataWithJSONObject:dict options:NSJSONWritingPrettyPrinted error:&error];
    NSString * jsonStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    if (error != nil) {
        return @"";
    } else {
        return jsonStr;
    }
}

@end
