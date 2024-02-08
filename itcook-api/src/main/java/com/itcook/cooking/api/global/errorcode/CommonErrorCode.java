package com.itcook.cooking.api.global.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum CommonErrorCode implements ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "400", "잘못된 요청입니다."),
    NULL_POINT(NOT_FOUND.value(), "404", "Null Point 에러입니다"),
    SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "500", "서버 에러입니다."),

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
