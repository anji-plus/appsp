package com.anji.appsp.sdktest.notice;

/**
 * 其中"dialog"和"scroll"是产品和开发约定好的type，根据type决定显示的风格
 */
public enum NoticeEnum {
    Normal(""),
    Dialog(NoticeType.Dialog),
    Scroll(NoticeType.Scroll);

    private String type;

    NoticeEnum(String type) {
        this.type = type;
    }
}
