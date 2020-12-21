import 'package:flutter/material.dart';

import 'confirm_dialog.dart';

class DialogUtils {
  static showCommonDialog(BuildContext parentContext,
      {String positiveMsg = '',
      String negativeMsg = '',
      Function onDone,
      Function onCancel,
      String msg = ''}) {
    showDialog(
        context: parentContext,
        builder: (context) {
          return showCustomCommonDialog(parentContext, context,
              positiveMsg: positiveMsg,
              negativeMsg: negativeMsg,
              onDone: onDone,
              onCancel: onCancel,
              msg: msg);
        }).then((result) {});
  }

  static Widget showCustomCommonDialog(
      BuildContext parentContext, BuildContext context,
      {String positiveMsg = '',
      String negativeMsg = '',
      Function onDone,
      Function onCancel,
      String msg = ''}) {
    ConfirmDialog messageDialog = new ConfirmDialog(
      title: "",
      message: msg,
      positiveText: positiveMsg,
      negativeText: negativeMsg,
      onPositivePressEvent: (str) {
        if (onDone != null) {
          onDone();
        }
        Navigator.pop(context);
      },
      onCloseEvent: () {
        if (onCancel != null) {
          onCancel();
        }
        Navigator.pop(context);
      },
      minHeight: 70,
    );
    return messageDialog;
  }

}
