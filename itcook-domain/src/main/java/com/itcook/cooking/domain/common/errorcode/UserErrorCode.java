package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode{

    TOKEN_EXPIRED(401, "U-001","해당 토큰이 만료되었습니다."),
    TOKEN_NOT_VALID(401, "U-002", "해당 토큰은 유효하지 않습니다."),
    USER_NOT_FOUND(400, "U-003","유저를 찾을 수 없습니다."),
    TOKEN_NOT_EXIST(401, "U-004", "토큰이 존재하지 않습니다."),
    NOT_EQUAL_REFRESH_TOKEN(401, "U-005", "리프레쉬 토큰이 일치하지 않습니다."),
    IS_LOGOUT_TOKEN(400, "U-006", "이미 로그아웃된 토큰입니다."),
    ALREADY_EXISTS_USER(400, "U-007", "이미 가입한 유저입니다."),
    EMAIL_VERIFY_FAIL(401, "U-008", "인증 코드가 일치하지 않습니다."),
    NO_VERIFY_CODE(401,"U-009" ,"인증 요청을 먼저 해주세요."),
    ALREADY_EXISTS_NICKNAME(400, "U-010", "이미 존재하는 닉네임입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
