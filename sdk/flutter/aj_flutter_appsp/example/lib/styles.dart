import 'package:flutter/material.dart';

import 'button_factory.dart';

class Styles {
  //默认的背景阴影和半径
  static const BoxDecoration cardDecoration = const BoxDecoration(
      color: Colors.white,
      boxShadow: [
        BoxShadow(color: Color(0x1A5b74FF), blurRadius: 4, spreadRadius: 2.4)
      ],
      borderRadius: BorderRadius.all(Radius.circular(8.0)));

  static Widget getBtn(BuildContext context, String title, Function callback) {
//    return InkWell(
//        child: Container(
//            child: Text(title, style: TextStyle(color: Color(0xFF00349c))),
//            height: 40,
//            width: MediaQuery.of(context).size.width - 100,
//            alignment: Alignment.center,
//            margin: EdgeInsets.only(bottom: 12),
//            decoration: Styles.cardDecoration),
//        onTap: () {
//          if (callback != null) {
//            callback();
//          }
//        });

    return  ButtonFactory.getRoundLargeBtn(
      context,
      backgroundColor: Colors.white,
      text: title,
      textColor: Colors.white,
      onTap: () {
        if (callback != null) {
          callback();
        }
      },
    );
  }
}
