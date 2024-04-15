package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArchiveErrorCode implements ErrorCode {

    ARCHIVE_NOT_EXIST(BAD_REQUEST.code, "A-001", "보관함이 비어있습니다."),
    ALREADY_ADD_ARCHIVE(BAD_REQUEST.code, "A-002", "이미 보관함에 추가된 게시글입니다."),
    NOT_SAVED_IN_ARCHIVE(BAD_REQUEST.code, "A-003", "보관함에 없는 게시글입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
