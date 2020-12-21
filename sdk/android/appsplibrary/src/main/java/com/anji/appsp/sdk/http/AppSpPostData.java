package com.anji.appsp.sdk.http;

import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 请求基础实体
 * </p>
 */
public class AppSpPostData extends LinkedHashMap {
    private static final long serialVersionUID = -3918611306392239969L;


    @Override
    public String toString() {
        String regLeft = "\"" + "\\{";
        String regRight = "\\}" + "\"";
        return new JSONObject(this).toString().replaceAll("\\\\", "")
                .replaceAll(regLeft, "{").replaceAll(regRight, "}");
    }
}
