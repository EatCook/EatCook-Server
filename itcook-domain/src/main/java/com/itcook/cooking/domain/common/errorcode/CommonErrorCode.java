package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;
import static com.itcook.cooking.domain.common.constant.StatusCode.INTERNAL_SERVER_ERROR;
import static com.itcook.cooking.domain.common.constant.StatusCode.NOT_FOUND;

import com.itcook.cooking.domain.common.constant.StatusCode;
import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    BAD_REQUEST(StatusCode.BAD_REQUEST.code, "400", "잘못된 요청입니다."),
    NULL_POINT(NOT_FOUND.code, "404", "Null Point 에러입니다"),
    SERVER_ERROR(INTERNAL_SERVER_ERROR.code, "500", "서버 에러입니다."),

    // 회원관련 에러코드 "M-001"

    ;


    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;

    CommonErrorCode(Integer httpStatusCode, String errorCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.description = description;
    }
}
