package com.itcook.cooking.domain.common.exception;

import com.itcook.cooking.domain.common.errorcode.ErrorCode;

public interface GlobalException {
    ErrorCode getErrorCode();
    String getErrorDescription();
}
