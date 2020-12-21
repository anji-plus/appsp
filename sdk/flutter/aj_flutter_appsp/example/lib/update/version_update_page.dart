import 'dart:io';
import 'dart:ui';
import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

import 'package:aj_flutter_appsp_example/dialog_utils.dart';
import 'package:aj_flutter_appsp_example/styles.dart';
import 'package:aj_flutter_appsp_example/update/version_update_dialog.dart';
import 'package:aj_flutter_appsp_example/update/version_update_dialog_new.dart';

///版本更新页面
class VersionUpdatePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(body: VersionUpdateWidget());
  }
}

class VersionUpdateWidget extends StatefulWidget {
  VersionUpdateWidget({Key key}) : super(key: key);

  @override
  _VersionUpdateState createState() => _VersionUpdateState();
}

enum UpdateType {
  Normal, //正常更新，不改造数据
  Force, //改造数据，模拟强制更新效果
  NotForce, //改造数据，模拟非强制更新效果
  ForceH5, //改造数据，模拟强制更新时跳转到外部网页下载的效果
  NotForceH5, //改造数据，模拟非强制更新时跳转到外部网页下载的效果
}

class _VersionUpdateState extends State<VersionUpdateWidget> {

  @override
  void initState() {
    super.initState();
  }

  _update(UpdateType updateType) async {
    //无需改造数据，用服务器返回数据，下面的都是模拟的数据
    //ignore
    SpRespUpdateModel updateModel =
        await AjFlutterAppSp.getUpdateModel();
    if (!mounted) {
      return;
    }
    if (updateModel == null) {
      Scaffold.of(context).showSnackBar(
        SnackBar(content: Text("没有更新信息")),
      );
      return;
    }
    print("spUpdateModel is $updateModel ");
    String errorMsg = null;
    if (AppSpStatusCode.StatusCode_Success != updateModel.repCode) {
      errorMsg = updateModel.repMsg;
    }

    if (errorMsg != null) {
      Scaffold.of(context).showSnackBar(
        SnackBar(content: Text(errorMsg)),
      );
    } else {
      _handleUpdate(updateModel, updateType);
    }
  }

  void _handleUpdate(SpRespUpdateModel respModel, UpdateType updateType) {
    if (respModel == null) {
      return;
    }
    //是否是外部链接，如果是，跳转到外部H5，否则直接下载
    bool isExternalUrl = true;
    UpdateModel updateModel = respModel.repData;
    if (updateModel == null) {
      Scaffold.of(context).showSnackBar(
        SnackBar(content: Text("当前为最新版本")),
      );
      return;
    }
    //apk结尾的默认为可直接下载，否认当作一个外部网页
    if (updateModel != null &&
        updateModel.downloadUrl != null &&
        updateModel.downloadUrl.contains(".apk")) {
      isExternalUrl = false;
    }
    if (updateType != null) {
      switch (updateType) {
        case UpdateType.Normal:
          //无需改造数据，用服务器返回数据，下面的都是模拟的数据
          break;
        case UpdateType.Force:
          //是外部地址，需要跳转H5
          isExternalUrl = false;
          //强制更新
          updateModel.mustUpdate = true;
          break;
        case UpdateType.NotForce:
          isExternalUrl = false;
          updateModel.mustUpdate = false;
          break;
        case UpdateType.ForceH5:
          isExternalUrl = true;
          updateModel.downloadUrl =
              "https://shouji.baidu.com/software/27007946.html";
          updateModel.mustUpdate = true;
          break;
        case UpdateType.NotForceH5:
          isExternalUrl = true;
          updateModel.downloadUrl =
              "https://shouji.baidu.com/software/27007946.html";
          updateModel.mustUpdate = false;
          break;
        default:
          break;
      }
    }
    if (updateModel.showUpdate != null && !updateModel.showUpdate) {
      Scaffold.of(context).showSnackBar(
        SnackBar(content: Text("当前为最新版本")),
      );
      return;
    }
    _versionUpdate(
        context: context,
        url: updateModel.downloadUrl,
        updateLog: updateModel.updateLog,
        mustUpdate: updateModel.mustUpdate,
        isExternalUrl: isExternalUrl);
  }

  ///url: url or
  _versionUpdate(
      {BuildContext context,
      String url,
      String updateLog,
      bool mustUpdate = false,
      bool isExternalUrl = false,
      Color buttonColor = Colors.blue,
      Color titleColor = const Color(0xFFFFA033)}) async {
    if (Platform.isIOS) {
      _showUpdateDialog(context, url, updateLog, mustUpdate, isExternalUrl,
          buttonColor: buttonColor, titleColor: titleColor);
      return;
    }
    final List<PermissionGroup> permissions = <PermissionGroup>[
      PermissionGroup.storage
    ];
    final Map<PermissionGroup, PermissionStatus> permissionRequestResult =
        await PermissionHandler().requestPermissions(permissions);
    PermissionStatus status = permissionRequestResult[PermissionGroup.storage];

    if (status == PermissionStatus.granted) {
      _showUpdateDialog(context, url, updateLog, mustUpdate, isExternalUrl,
          buttonColor: buttonColor, titleColor: titleColor);
    } else {
      DialogUtils.showCommonDialog(context,
          msg: '"获取文件读写权限失败,即将跳转应用信息”>“权限”中开启权限"',
          negativeMsg: '取消',
          positiveMsg: '前往', onDone: () {
        PermissionHandler().openAppSettings().then((openSuccess) {
          if (openSuccess != true) {}
        });
      });
    }
  }

  _showUpdateDialog(BuildContext context, String url, String releaseLog,
      bool mustUpdate, bool isExternalUrl,
      {Color buttonColor, Color titleColor}) {
    List<String> logs = [];
    logs.add(releaseLog);
    showDialog(
        context: context,
        builder: (context) {
          // VersionUpdateDialog messageDialog = new VersionUpdateDialog(
          //周阳 修改
          VersionUpdateDialogNew messageDialog = new VersionUpdateDialogNew(
            positiveText: "更新",
            versionMsgList: logs,
            mustUpdate: mustUpdate,
            url: url,
            isExternalUrl: isExternalUrl,
            buttonColor: buttonColor,
            titleColor: titleColor,
          );
          return messageDialog;
        });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('版本更新'),
      ),
      body: Column(
        children: <Widget>[
          SizedBox(
            height: 20,
          ),
          Center(
            child: Styles.getBtn(context, '检查版本更新', () {
              _update(UpdateType.Normal);
            }),
          ),
          SizedBox(
            height: 10,
          ),
          Styles.getBtn(context, '模拟强制更新', () {
            _update(UpdateType.Force);
          }),
          SizedBox(
            height: 10,
          ),
          Styles.getBtn(context, '模拟非强制更新', () {
            _update(UpdateType.NotForce);
          }),
          SizedBox(
            height: 10,
          ),
          Styles.getBtn(context, '模拟强制更新-外部网页下载', () {
            _update(UpdateType.ForceH5);
          }),
          SizedBox(
            height: 10,
          ),
          Styles.getBtn(context, '模拟非强制更新-外部网页下载', () {
            _update(UpdateType.NotForceH5);
          }),
        ],
      ),
    );
  }
}
