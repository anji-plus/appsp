class SpNoticeModelItem {
  String title; //公告标题
  String details; //公告内容
  String templateType; //模板风格
  String templateTypeName; //模板名称

  SpNoticeModelItem(
      {this.title, this.details, this.templateType, this.templateTypeName});

  SpNoticeModelItem.fromJson(Map<String, dynamic> json) {
    title = json['title'];
    details = json['details'];
    templateType = json['templateType'];
    templateTypeName = json['templateTypeName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['title'] = this.title;
    data['details'] = this.details;
    data['templateType'] = this.templateType;
    data['templateTypeName'] = this.templateTypeName;
    return data;
  }

  @override
  String toString() {
    return 'SpNoticeModelItem{title: $title, details: $details, templateType: $templateType, templateTypeName: $templateTypeName}';
  }


}
