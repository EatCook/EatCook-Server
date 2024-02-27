package com.itcook.cooking.domain.common.errorcode;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    BAD_REQUEST(400, "400", "잘못된 요청입니다."),
    NULL_POINT(404, "404", "Null Point 에러입니다"),
    SERVER_ERROR(500, "500", "서버 에러입니다."),

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
