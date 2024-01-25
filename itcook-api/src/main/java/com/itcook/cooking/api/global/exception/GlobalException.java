package com.itcook.cooking.api.global.exception;

import com.itcook.cooking.api.global.errorcode.ErrorCode;

public interface GlobalException {
    ErrorCode getErrorCode();
    String getErrorDescription();
}
