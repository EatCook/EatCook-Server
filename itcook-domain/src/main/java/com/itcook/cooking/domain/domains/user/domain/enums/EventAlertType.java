package com.itcook.cooking.domain.domains.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventAlertType {
    DISABLED("disabled"),
    ACTIVATE("activate")
    ;

    private final String eventAlertTypeName;

}
