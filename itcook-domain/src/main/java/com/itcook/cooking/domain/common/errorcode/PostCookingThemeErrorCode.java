package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum PostCookingThemeErrorCode implements ErrorCode {
    POST_COOKING_THEME_NOT_FOUND(BAD_REQUEST.code, "PCT-001", "미등록한 관심 요리입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
