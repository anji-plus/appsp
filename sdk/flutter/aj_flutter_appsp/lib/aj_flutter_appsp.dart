import 'aj_flutter_appsp_lib.dart';

import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';

/// Copyright © 2018 anji-plus
/// 安吉加加信息技术有限公司
/// http:///www.anji-plus.com
/// All rights reserved.
/// AppSp功能
class AjFlutterAppSp {
  static const MethodChannel _channel = const MethodChannel('aj_flutter_appsp');

  ///设置基础地址，如果在开发测试场景会用到，生产时候记得改成生产地址，或者最好不要对暴露set方法
  ///[appKey] 应用唯一标识
  ///[host] 设置请求基础地址
  ///[debug] 是否打开日志开关，true为打开
  static Future<String> init(
      {String appKey, String host, bool debug = true}) async {
    final String result = await _channel.invokeMethod(
        'init', {"appKey": appKey, "host": host, "debug": debug});
    return result;
  }

  ///获取版本信息
  static Future<SpRespUpdateModel> getUpdateModel() async {
    final String jsonStr = await _channel.invokeMethod('getUpdateModel');
    SpRespUpdateModel updateModel =
    SpRespUpdateModel.fromJson(json.decode(jsonStr));
    return updateModel;
  }

  ///获取公告信息
  static Future<SpRespNoticeModel> getNoticeModel() async {
    final String jsonStr = await _channel.invokeMethod('getNoticeModel');
    SpRespNoticeModel noticeModel =
    SpRespNoticeModel.fromJson(json.decode(jsonStr));
    return noticeModel;
  }
}
