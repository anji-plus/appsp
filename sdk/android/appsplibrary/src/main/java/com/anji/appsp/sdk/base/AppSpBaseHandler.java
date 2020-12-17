package com.anji.appsp.sdk.base;

public class AppSpBaseHandler {
    protected String getStringElement(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        return "";
    }

    protected Boolean getBooleanElement(Object object) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        return false;
    }

}
