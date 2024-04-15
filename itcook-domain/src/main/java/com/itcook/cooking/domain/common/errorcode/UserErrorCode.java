package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;
import static com.itcook.cooking.domain.common.constant.StatusCode.UNAUTHORIZED;

import com.itcook.cooking.domain.common.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode{

    TOKEN_EXPIRED(UNAUTHORIZED.code, "U-001","해당 토큰이 만료되었습니다."),
    TOKEN_NOT_VALID(UNAUTHORIZED.code, "U-002", "해당 토큰은 유효하지 않습니다."),
    USER_NOT_FOUND(BAD_REQUEST.code, "U-003","유저를 찾을 수 없습니다."),
    TOKEN_NOT_EXIST(UNAUTHORIZED.code, "U-004", "토큰이 존재하지 않습니다."),
    NOT_EQUAL_REFRESH_TOKEN(UNAUTHORIZED.code, "U-005", "리프레쉬 토큰이 일치하지 않습니다."),
    IS_LOGOUT_TOKEN(BAD_REQUEST.code, "U-006", "이미 로그아웃된 토큰입니다."),
    ALREADY_EXISTS_USER(BAD_REQUEST.code, "U-007", "이미 가입한 유저입니다."),
    EMAIL_VERIFY_FAIL(UNAUTHORIZED.code, "U-008", "인증 코드가 일치하지 않습니다."),
    NO_VERIFY_CODE(UNAUTHORIZED.code,"U-009" ,"인증 요청을 먼저 해주세요."),
    ALREADY_EXISTS_NICKNAME(BAD_REQUEST.code, "U-010", "이미 존재하는 닉네임입니다."),
    ALREADY_FOLLOW_USER(BAD_REQUEST.code, "U-011", "이미 팔로우한 유저입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
