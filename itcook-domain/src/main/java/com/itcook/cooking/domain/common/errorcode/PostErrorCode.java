package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;
import static com.itcook.cooking.domain.common.constant.StatusCode.OK;

import com.itcook.cooking.domain.common.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_EXIST(BAD_REQUEST.code, "P-001", "조회된 레시피가 없습니다."),
    POST_200_NOT_EXIST(OK.code, "P-002", "조회된 레시피가 없습니다."),
    POST_REQUEST_ERROR(BAD_REQUEST.code, "P-002", "요청된 데이터에 문제가 있습니다"),
    POST_FILE_EXTENSION_NOT_EXIST(BAD_REQUEST.code, "P-003", "허용되지 않은 파일 확장자입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
