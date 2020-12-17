import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ConfirmDialogWidget extends StatefulWidget {
  String title;
  String message;
  String negativeText;
  String positiveText;
  Function onCloseEvent;
  Function onPositivePressEvent;
  Function keybackEvent;
  double minHeight;
  double barHeight = 48;
  double radius = 20;
  double msgFontSize;

  ConfirmDialogWidget(
      {@required this.title,
      @required this.message,
      this.negativeText,
      this.positiveText,
      this.msgFontSize = 15,
      this.onPositivePressEvent,
      @required this.onCloseEvent,
      this.keybackEvent,
      this.minHeight = 60});

  @override
  State<StatefulWidget> createState() {
    return new _ConfirmDialogWidgetState();
  }
}

class _ConfirmDialogWidgetState extends State<ConfirmDialogWidget> {
  Widget getContent() {
    return Row(
      children: <Widget>[
        SizedBox(
          width: 16,
        ),
        Expanded(
          child: Center(
            child: new Text(
              widget.message,
              style: TextStyle(
                fontSize: widget.msgFontSize ?? 16.0,
                color: Color(0xff666666),
              ),
              maxLines: 2,
              softWrap: true,
              overflow: TextOverflow.ellipsis,
            ),
          ),
          flex: 1,
        ),
        SizedBox(
          width: 16,
        ),
      ],
      mainAxisAlignment: MainAxisAlignment.center,
    );
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return WillPopScope(
        child: new Padding(
          padding: const EdgeInsets.only(left: 40.0, right: 40.0),
          child: new Material(
            type: MaterialType.transparency,
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
//              margin: const EdgeInsets.all(12.0),
                  child: new Column(
                    children: <Widget>[
                      new Container(
                        constraints:
                            BoxConstraints(minHeight: widget.minHeight),
                        child: new Padding(
                          padding: const EdgeInsets.all(0.0),
                          child: new Center(child: getContent()),
                        ),
                      ),
                      getDivider(),
                      this._buildOperationBar(),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
        onWillPop: () async {
          if (widget.keybackEvent != null) {
            widget.keybackEvent();
          } else {
            Navigator.of(context).pop();
          }
          return false;
        });
  }

  Widget getDivider({double height = 1.0}) {
    return Container(
      color: Color(0xFFf0f0f0),
      height: height,
    );
  }

  Widget _buildOperationBar() {
    if (widget.onCloseEvent == null) {
      return Material(
          color: Colors.white,
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
                child: Text(
                  widget.positiveText,
                  style: TextStyle(
                      color: Theme.of(context).primaryColor, fontSize: 16.0),
                ),
              ),
              onTap: () {
                widget.onPositivePressEvent("hhhhhh");
              },
              borderRadius: BorderRadius.only(
                bottomLeft: Radius.circular(widget.radius),
                bottomRight: Radius.circular(widget.radius),
              ),
            ),
          ));
    }
    return Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          new Expanded(
            flex: 1,
            child: _getNegativeWidget(),
          ),
          new Expanded(
            flex: 1,
            child: _getPositiveWidget(),
          ),
        ]);
  }

  Widget _getNegativeWidget() {
    return Material(
        color: Colors.white,
        shape: RoundedRectangleBorder(
          borderRadius:
              BorderRadius.only(bottomLeft: Radius.circular(widget.radius)),
        ),
        child: Container(
          alignment: Alignment.center,
          height: widget.barHeight,
          child: InkWell(
              child: Center(
                child: Text(widget.negativeText,
                    style: TextStyle(color: Color(0xFF333333), fontSize: 16.0)),
              ),
              onTap: widget.onCloseEvent,
              borderRadius: BorderRadius.only(
                  bottomLeft: Radius.circular(widget.radius))),
        ));
  }

  Widget _getPositiveWidget() {
    return Material(
        color: Theme.of(context).primaryColor,
        shape: RoundedRectangleBorder(
          borderRadius:
              BorderRadius.only(bottomRight: Radius.circular(widget.radius)),
        ),
        child: Container(
          alignment: Alignment.center,
          height: widget.barHeight,
          child: InkWell(
              child: Center(
                child: Text(widget.positiveText,
                    style: TextStyle(color: Colors.white, fontSize: 16.0)),
              ),
              onTap: () {
                widget.onPositivePressEvent("hhhhhh");
              },
              borderRadius: BorderRadius.only(
                  bottomRight: Radius.circular(widget.radius))),
        ));
  }

  @override
  void initState() {
    super.initState();
  }
}

// ignore: must_be_immutable
class ConfirmDialog extends Dialog {
  String title;
  String message;
  String negativeText;
  String positiveText;
  double msgFontSize;
  Function onCloseEvent;
  Function onPositivePressEvent;
  Function keybackEvent;
  double minHeight;
  double barHeight = 48;
  double radius = 20;
  ConfirmDialog(
      {Key key,
      @required this.title,
      @required this.message,
      this.negativeText,
      this.positiveText,
      this.onPositivePressEvent,
      this.keybackEvent,
      this.msgFontSize,
      @required this.onCloseEvent,
      this.minHeight = 60})
      : assert(minHeight > 0),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return new ConfirmDialogWidget(
        title: title,
        message: message,
        negativeText: negativeText,
        positiveText: positiveText,
        msgFontSize: msgFontSize,
        onPositivePressEvent: onPositivePressEvent,
        onCloseEvent: onCloseEvent,
        keybackEvent: keybackEvent,
        minHeight: minHeight);
  }
}
