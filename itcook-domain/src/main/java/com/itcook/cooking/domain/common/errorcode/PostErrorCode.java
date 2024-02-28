package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_EXIST(401, "P-001", "조회된 레시피가 없습니다."),
    POST_NOT_FOUND(400, "P-002", "작성된 레시피가 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
