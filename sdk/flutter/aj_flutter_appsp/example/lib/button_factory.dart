import 'package:flutter/material.dart';

class ButtonFactory {
  //针对大的长的button，比如登录注册这种
  static Widget getRoundLargeBtn(BuildContext context,
      {bool enabled = true,
      Color backgroundColor,
      Color textColor,
      double height = 36,
      String text,
      onTap,
      double margin = 36,
      double fontSize = 15,
      double radius = 18,
      bool splashLight = true,
      double splashSpreadRadius = 3,
      double splashBlurRadius = 6,
      List<Color> highlightGradientColors = const <Color>[
        Color(0xFF5B74FF),
        Color(0xFF7A9CFF)
      ],
      List<Color> disableGradientColors = const <Color>[
        Color(0xFFC5C5C5),
        Color(0xFFDCDCDC)
      ],
      Color splashColor = const Color(0x445b74FF)}) {
    double width = MediaQuery.of(context).size.width - margin * 2;
    double extra = splashBlurRadius;
    double resultHeight = height + extra * 3;
    double resultWidth = width + extra;
    List<BoxShadow> boxShadows = [];
    if (splashLight == true) {
      boxShadows.add(BoxShadow(
          color: splashColor,
          blurRadius: splashBlurRadius,
          spreadRadius: splashSpreadRadius));
    }
    return Stack(
      children: <Widget>[
        Container(
          width: resultWidth,
          height: resultHeight,
          alignment: Alignment.center,
          child: Container(
            width: width,
            height: height,
            alignment: Alignment.center,
            decoration: BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(height / 2)),
                boxShadow: boxShadows),
          ),
        ),
        Material(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(radius)),
            ),
            color: backgroundColor,
            child: Container(
              width: width,
              height: height,
              alignment: Alignment.center,
              decoration: enabled
                  ? BoxDecoration(
                      gradient: enabled
                          ? LinearGradient(colors: highlightGradientColors)
                          : LinearGradient(colors: disableGradientColors),
                      borderRadius: BorderRadius.all(Radius.circular(radius)))
                  : null,
              child: RaisedButton(
                child: Center(
                  child: Text(text,
                      style: TextStyle(color: textColor, fontSize: fontSize)),
                ),
                color: Colors.transparent,
                elevation: 0,
                highlightElevation: 0,
                disabledColor: backgroundColor,
                highlightColor: Colors.transparent,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(radius)),
                ),
                onPressed: () {
                  if (onTap != null && enabled == true) {
                    onTap();
                  }
                },
              ),
            ))
      ],
      alignment: Alignment.center,
    );
  }

  //针对短的button，比如取消订单这种
  static Widget getRoundSmallBtn(
      {bool enabled = true,
      Color backgroundColor,
      Color textColor,
      double height = 36,
      String text,
      onTap,
      double width = 100,
      double fontSize = 15,
      bool bold = false,
      double radius = 18,
      bool splashLight = true,
      double splashSpreadRadius = 2.4,
      double splashBlurRadius = 4,
      List<Color> highlightGradientColors = const <Color>[
        Color(0xFF5B74FF),
        Color(0xFF7A9CFF)
      ],
      List<Color> disableGradientColors = const <Color>[
        Color(0xFFC5C5C5),
        Color(0xFFDCDCDC)
      ],
      Color splashColor = const Color(0x555b74FF)}) {
    double extra = splashBlurRadius;
    double resultHeight = height + extra * 3;
    double resultWidth = width + extra;
    List<BoxShadow> boxShadows = [];
    if (splashLight == true) {
      boxShadows.add(BoxShadow(
          color: splashColor,
          blurRadius: splashBlurRadius,
          spreadRadius: splashSpreadRadius));
    }
    return Stack(
      children: <Widget>[
        Container(
          width: resultWidth,
          height: resultHeight,
          alignment: Alignment.center,
          child: Container(
            width: width,
            height: height,
            alignment: Alignment.center,
            decoration: BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(height / 2)),
                boxShadow: boxShadows),
          ),
        ),
        Material(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(radius)),
            ),
            color: backgroundColor,
            child: Container(
              width: width,
              height: height,
              alignment: Alignment.center,
              decoration: enabled
                  ? BoxDecoration(
                      gradient: enabled
                          ? LinearGradient(colors: highlightGradientColors)
                          : LinearGradient(colors: disableGradientColors),
                      borderRadius: BorderRadius.all(Radius.circular(radius)))
                  : null,
              child: RaisedButton(
                child: Center(
                  child: Text(text,
                      style: TextStyle(
                          color: textColor,
                          fontSize: fontSize,
                          fontWeight: bold == false
                              ? FontWeight.normal
                              : FontWeight.bold)),
                ),
                color: Colors.transparent,
                elevation: 0,
                highlightElevation: 0,
                disabledColor: backgroundColor,
                highlightColor: Colors.transparent,
                onPressed: () {
                  if (onTap != null && enabled == true) {
                    onTap();
                  }
                },
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(radius)),
                ),
              ),
            ))
      ],
      alignment: Alignment.center,
    );
  }
}
