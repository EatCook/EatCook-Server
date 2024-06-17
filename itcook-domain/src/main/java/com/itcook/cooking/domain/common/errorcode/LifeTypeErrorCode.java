package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum LifeTypeErrorCode implements ErrorCode {
    LIFETYPE_NOT_FOUND(BAD_REQUEST.code, "LT-001", "잘못된 생활 유형 요청입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
