package com.itcook.cooking.api.global.errorcode;

public interface ErrorCode {
    Integer getHttpStatusCode();
    String getErrorCode();
    String getDescription();
}
