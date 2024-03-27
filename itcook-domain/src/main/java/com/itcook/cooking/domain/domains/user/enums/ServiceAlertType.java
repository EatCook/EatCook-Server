package com.itcook.cooking.domain.domains.user.enums;

import lombok.Getter;

@Getter
public enum ServiceAlertType {
    DISABLED("disabled"),
    ACTIVATE("activate")
    ;

    private final String serviceAlertTypeName;

    ServiceAlertType(String serviceAlertTypeName) {
        this.serviceAlertTypeName = serviceAlertTypeName;
    }
}
