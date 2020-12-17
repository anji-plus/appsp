class SpRespUpdateModel {
  String repCode;
  String repMsg;
  UpdateModel repData;

  SpRespUpdateModel({
    this.repCode,
    this.repMsg,
    this.repData,
  });

  SpRespUpdateModel.fromJson(Map<String, dynamic> json) {
    repCode = json['repCode'];
    repMsg = json['repMsg'];
    repData = json['repData'] != null
        ? new UpdateModel.fromJson(json['repData'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['repCode'] = this.repCode;
    data['repMsg'] = this.repMsg;
    if (this.repData != null) {
      data['repData'] = this.repData.toJson();
    }
    return data;
  }

  @override
  String toString() {
    return 'SpRespUpdateModel{repCode: $repCode, repMsg: $repMsg, repData: $repData}';
  }
}

class UpdateModel {
  bool mustUpdate;
  bool showUpdate;
  String updateLog;
  String downloadUrl;

  UpdateModel(
      {this.mustUpdate, this.showUpdate, this.updateLog, this.downloadUrl});

  UpdateModel.fromJson(Map<String, dynamic> json) {
    mustUpdate = json['mustUpdate'];
    showUpdate = json['showUpdate'];
    updateLog = json['updateLog'];
    downloadUrl = json['downloadUrl'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['mustUpdate'] = this.mustUpdate;
    data['showUpdate'] = this.showUpdate;
    data['updateLog'] = this.updateLog;
    data['downloadUrl'] = this.downloadUrl;
    return data;
  }

  @override
  String toString() {
    return 'UpdateModel{mustUpdate: $mustUpdate, showUpdate: $showUpdate, updateLog: $updateLog, downloadUrl: $downloadUrl}';
  }
}
