package com.itcook.cooking.domain.common.exception;

import com.itcook.cooking.domain.common.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements GlobalException {

    private final ErrorCode errorCode;
    private final String errorDescription;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorDescription = errorCode.getDescription();
    }

    public ApiException(ErrorCode errorCode, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
