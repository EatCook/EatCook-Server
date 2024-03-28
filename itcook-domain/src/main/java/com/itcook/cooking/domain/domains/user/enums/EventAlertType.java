package com.itcook.cooking.domain.domains.user.enums;

import lombok.Getter;

@Getter
public enum EventAlertType {
    DISABLED("disabled"),
    ACTIVATE("activate")
    ;

    private final String eventAlertTypeName;

    EventAlertType(String eventAlertTypeName) {
        this.eventAlertTypeName = eventAlertTypeName;
    }
}
