package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LikedErrorCode implements ErrorCode {
    ALREADY_ADD_Liked(BAD_REQUEST.code, "l-001", "이미 좋아요 한 게시글입니다."),
    NOT_SAVED_IN_LIKED(BAD_REQUEST.code, "l-002", "해당 게시글에 좋아요를 누르지 않았습니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
