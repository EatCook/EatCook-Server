package com.itcook.cooking.domain.common.errorcode;

import static com.itcook.cooking.domain.common.constant.StatusCode.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCode{

    NOTIFICATION_NOT_FOUND(BAD_REQUEST.code, "U-003","유저를 찾을 수 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorCode;
    private final String description;

}
