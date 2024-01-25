package com.itcook.cooking.api.global.errorcode;

public interface ErrorCode {
    Integer getHttpStatusCode();
    Integer getErrorCode();
    String getDescription();
}
