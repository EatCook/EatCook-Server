package com.itcook.cooking.domain.domains.user.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN")
    ;

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }
}
