import 'dart:ui';

import 'package:aj_flutter_appsp/aj_flutter_appsp_lib.dart';
import 'package:flutter/material.dart';
import 'package:marquee/marquee.dart';

import 'package:aj_flutter_appsp_example/notice/notice_dialog.dart';
import 'package:aj_flutter_appsp_example/notice/notice_type.dart';
import 'package:aj_flutter_appsp_example/styles.dart';

///公告页
class NoticePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return NoticeWidget();
  }
}

class NoticeWidget extends StatefulWidget {
  NoticeWidget({Key key}) : super(key: key);

  @override
  _NoticeState createState() => _NoticeState();
}

class _NoticeState extends State<NoticeWidget> {
  List<SpNoticeModelItem> marqueeeItems = [];
  var _scaffoldkey = new GlobalKey<ScaffoldState>();

  @override
  void initState() {
    super.initState();
  }

  _requestNoticeType(String noticeType) async {
    //无需改造数据，用服务器返回数据，下面的都是模拟的数据
    //ignore
    SpRespNoticeModel noticeModel = await AjFlutterAppSp.getNoticeModel();
    if (!mounted) {
      return;
    }
    if (noticeModel == null ||
        noticeModel.repData == null ||
        noticeModel.repData.isEmpty) {
      var snackBar = SnackBar(content: Text("没有公告信息"));
      _scaffoldkey.currentState.showSnackBar(snackBar);
      return;
    }
    String errorMsg = null;
    if (AppSpStatusCode.StatusCode_Success != noticeModel.repCode) {
      errorMsg = noticeModel.repMsg;
    }
    if (errorMsg != null) {
      var snackBar = SnackBar(content: Text(errorMsg));
      _scaffoldkey.currentState.showSnackBar(snackBar);
      return;
    } else {
      _handleNotice(noticeModel.repData, noticeType);
    }
  }

  _handleNotice(List<SpNoticeModelItem> modelItems, String noticeType,
      {Color buttonColor, Color titleColor = const Color(0xFFFFA033)}) {
    if (modelItems == null) {
      return;
    }
    if (noticeType == null) {
      //常规场景，不做模拟
      marqueeeItems.clear();
      modelItems.forEach((modelItem) {
        if (modelItem.templateType == NoticeType.Dialog) {
          _showNoticeDialog(modelItem,
              buttonColor: buttonColor, titleColor: titleColor);
        } else if (modelItem.templateType == NoticeType.Marquee) {
          marqueeeItems.add(modelItem);
        }
      });
      setState(() {});
      return;
    }
    //一下是模拟
    switch (noticeType) {
      case NoticeType.Dialog: //如果显示样式是dialog
        modelItems.forEach((modelItem) {
          modelItem.templateType = NoticeType.Dialog;
          _showNoticeDialog(modelItem,
              buttonColor: buttonColor, titleColor: titleColor);
        });
        break;
      case NoticeType.Marquee: //如果显示样式是跑马灯
        setState(() {
          modelItems.forEach((modelItem) {
            modelItem.templateType = NoticeType.Marquee;
          });
          marqueeeItems = modelItems;
        });
        break;
      default:
        break;
    }
  }

  _showNoticeDialog(SpNoticeModelItem noticeItem,
      {Color buttonColor, Color titleColor}) {
    showDialog(
        context: context,
        builder: (context) {
          NoticeDialog noticeDialog = new NoticeDialog(
            noticeItem: noticeItem,
            positiveText: "更新",
            buttonColor: buttonColor,
            titleColor: titleColor,
          );
          return noticeDialog;
        });
  }

  List<Widget> getMarqueenWidgets() {
    List<Widget> widgets = [];
    if (marqueeeItems.isEmpty) {
      return widgets;
    }
    marqueeeItems?.forEach((item) {
      Widget child = Container(
        child: Row(
          children: <Widget>[
            Image.asset(
              'images/alert.png',
              width: 20,
              height: 20,
            ),
            Padding(
              padding: EdgeInsets.all(16.0),
              child: Container(
                  height: 30.0,
                  width: MediaQuery.of(context).size.width - 100,
                  child: Marquee(
                    text: item.details,
                    blankSpace: 60.0,
                  )),
            )
          ],
          mainAxisAlignment: MainAxisAlignment.center,
        ),
        color: Colors.white,
      );
      widgets.add(child);
    });
    return widgets;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _scaffoldkey,
      appBar: AppBar(
        title: const Text('公告样式'),
      ),
      body: Column(
        children: <Widget>[
          SizedBox(
            height: 20,
          ),
          Center(
              child: Styles.getBtn(context, '显示公告', () {
            _requestNoticeType(null);
          })),
          Center(
              child: Styles.getBtn(context, '模拟弹窗公告', () {
            _requestNoticeType(NoticeType.Dialog);
          })),
          SizedBox(
            height: 10,
          ),
          Styles.getBtn(context, '模拟跑马灯公告', () {
            _requestNoticeType(NoticeType.Marquee);
          }),
          SizedBox(
            height: 10,
          ),
          Column(children: getMarqueenWidgets())
        ],
      ),
    );
  }
}
