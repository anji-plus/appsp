package com.anji.sp.exception;


public class BadPasswordUpperLimitException extends RuntimeException {
    public BadPasswordUpperLimitException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadPasswordUpperLimitException(String msg) {
        super(msg);
    }
}
