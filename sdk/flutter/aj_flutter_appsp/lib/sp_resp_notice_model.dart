import 'sp_notice_model_item.dart';

class SpRespNoticeModel {
  List<SpNoticeModelItem> repData;
  String repCode;
  String repMsg;

  SpRespNoticeModel({this.repData, this.repCode, this.repMsg});

  SpRespNoticeModel.fromJson(Map<String, dynamic> json) {
    if (json['repData'] != null) {
      repData = new List<SpNoticeModelItem>();
      json['repData'].forEach((v) {
        repData.add(new SpNoticeModelItem.fromJson(v));
      });
    }
    repCode = json['repCode'];
    repMsg = json['repMsg'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.repData != null) {
      data['repData'] =
          this.repData.map((v) => v.toJson()).toList();
    }
    data['repCode'] = this.repCode;
    data['repMsg'] = this.repMsg;
    return data;
  }

  @override
  String toString() {
    return 'SpRespNoticeModel{repData: $repData, repCode: $repCode, repMsg: $repMsg}';
  }
}
