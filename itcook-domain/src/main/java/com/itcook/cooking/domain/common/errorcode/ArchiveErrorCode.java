package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArchiveErrorCode implements ErrorCode {

    ARCHIVE_NOT_EXIST(401, "A-001", "보관함이 비어있습니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
