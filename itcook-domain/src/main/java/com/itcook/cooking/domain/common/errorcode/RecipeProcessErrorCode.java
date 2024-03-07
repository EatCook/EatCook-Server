package com.itcook.cooking.domain.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipeProcessErrorCode implements ErrorCode {

    RECIPE_PROCESS_REQUEST_ERROR(401, "RP-001", "요청된 조리 과정 데이터에 문제가 있습니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
