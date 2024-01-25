package com.itcook.cooking.api.global.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청입니다."),
    NULL_POINT(HttpStatus.NOT_FOUND.value(), 404, "Null Point 에러입니다"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버 에러입니다."),

    ;


    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

    CommonErrorCode(Integer httpStatusCode, Integer errorCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.description = description;
    }
}
