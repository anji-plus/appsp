import 'dart:ui';

import 'package:aj_flutter_appsp/sp_notice_model_item.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:aj_flutter_appsp_example/commons.dart';

// ignore: must_be_immutable
class NoticeDialog extends Dialog {
  SpNoticeModelItem noticeItem;
  String negativeText;
  String positiveText;
  double minHeight;
  Color buttonColor;
  Color titleColor;
  double barHeight = 48;
  double radius = 20;

  NoticeDialog(
      {Key key,
      this.noticeItem,
      this.negativeText,
      this.positiveText,
      this.minHeight = 60,
      this.buttonColor,
      this.titleColor})
      : assert(minHeight > 0),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return new NoticeWidget(
      noticeItem: noticeItem,
      negativeText: negativeText,
      positiveText: positiveText,
      minHeight: minHeight,
      titleColor: titleColor,
      buttonColor: buttonColor,
    );
  }
}

class NoticeWidget extends StatefulWidget {
  SpNoticeModelItem noticeItem;
  String negativeText;
  String positiveText;
  double minHeight;
  Color titleColor;
  Color buttonColor;
  double barHeight = 48;
  double radius = 20;

  NoticeWidget({
    Key key,
    this.noticeItem,
    this.negativeText,
    this.positiveText,
    this.minHeight = 60,
    this.titleColor,
    this.buttonColor = Colors.white,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return new _NoticeWidgetState();
  }
}

class _NoticeWidgetState extends State<NoticeWidget> {
  static const apkInstallChannel =
      const MethodChannel(Commons.apkinstallChannel);
  static const String apkInstallMethod = Commons.apkInstallMethod;

  @override
  void initState() {
    super.initState();
  }

  Widget getNoticeDetailsLayout() {
    var scale = MediaQuery.of(context).textScaleFactor;
    return Container(
      constraints: BoxConstraints(minHeight: 24),
      child: new Text(
        widget.noticeItem?.details ?? "",
        style: TextStyle(
            fontSize: 15.0 / scale,
            color: Color(0xFF666666),
            fontWeight: FontWeight.normal,
            decoration: TextDecoration.none),
      ),
      alignment: Alignment.centerLeft,
      padding: EdgeInsets.only(left: 12, right: 12),
    );
  }

  Widget getDivider({double height = 1.0}) {
    return Container(
      color: Color(0xFFf0f0f0),
      height: height,
    );
  }

  Widget _buildOperationBar() {
    return Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          new Expanded(
            flex: 1,
            child: _getOperateWidget(),
          )
        ]);
  }

  Widget _getOperateWidget() {
    var scale = MediaQuery.of(context).textScaleFactor;
    return Material(
        color: widget.buttonColor,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.only(
              bottomLeft: Radius.circular(widget.radius),
              bottomRight: Radius.circular(widget.radius)),
        ),
        child: Container(
          alignment: Alignment.center,
          height: widget.barHeight,
          child: InkWell(
            child: Center(
              child: Text('我知道了',
                  style: TextStyle(
                      color: Color(0xFF333333), fontSize: 16.0 / scale)),
            ),
            onTap: () {
              Navigator.of(context).pop();
            },
            borderRadius: BorderRadius.only(
                bottomLeft: Radius.circular(widget.radius),
                bottomRight: Radius.circular(widget.radius)),
          ),
        ));
  }

  @override
  Widget build(BuildContext context) {
    var scale = MediaQuery.of(context).textScaleFactor;
    return new WillPopScope(
      child: new Padding(
        padding: const EdgeInsets.only(left: 40.0, right: 40.0),
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Container(
              decoration: ShapeDecoration(
                color: Color(0xffffffff),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(
                    Radius.circular(widget.radius),
                  ),
                ),
              ),
              child: new Column(
                children: <Widget>[
                  new Container(
                    child: new Text(
                      widget.noticeItem?.title ?? "",
                      style: TextStyle(
                          fontSize: 20.0 / scale,
                          color: widget.titleColor,
                          fontWeight: FontWeight.normal,
                          decoration: TextDecoration.none),
                    ),
                    height: 36,
                    margin: EdgeInsets.only(top: 6),
                    alignment: Alignment.center,
                  ),
                  new Container(
                    constraints: BoxConstraints(minHeight: widget.minHeight),
                    child: getNoticeDetailsLayout(),
                  ),
                  getDivider(),
                  this._buildOperationBar(),
                ],
              ),
            ),
          ],
        ),
      ),
      onWillPop: () {
        Navigator.of(context).pop();
      },
    );
  }
}
