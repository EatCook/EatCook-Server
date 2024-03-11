package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_EXIST(401, "P-001", "조회된 레시피가 없습니다."),
    POST_REQUEST_ERROR(400, "P-002", "요청된 데이터에 문제가 있습니다"),
    POST_FILE_EXTENSION_NOT_EXIST(400, "P-003", "허용되지 않은 파일 확장자입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
