package com.itcook.cooking.domain.domains.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceAlertType {
    DISABLED("disabled"),
    ACTIVATE("activate")
    ;

    private final String serviceAlertTypeName;

}
