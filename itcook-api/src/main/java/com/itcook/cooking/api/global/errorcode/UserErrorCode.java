package com.itcook.cooking.api.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode{

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "U-001","해당 토큰이 만료되었습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED.value(), "U-002", "해당 토큰은 유효하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "U-003","유저를 찾을 수 없습니다."),
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED.value(), "U-004", "토큰이 존재하지 않습니다."),
    NOT_EQUAL_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "U-005", "리프레쉬 토큰이 일치하지 않습니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
