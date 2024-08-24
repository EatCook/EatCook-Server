package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;

import com.itcook.cooking.domain.common.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipeProcessErrorCode implements ErrorCode {

    INVALID_STEPNUM_ORDER(BAD_REQUEST.code, "RP-001", "조리 과정의 순서가 잘 못 되었습니다."),
    INVALID_IMAGE_EXTENSION(BAD_REQUEST.code, "RP-002", "기존에 등록된 이미지가 아닙니다.");

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;
}
