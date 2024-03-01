package com.itcook.cooking.domain.common.errorcode;

public interface ErrorCode {
    Integer getHttpStatusCode();
    String getErrorCode();
    String getDescription();
}
