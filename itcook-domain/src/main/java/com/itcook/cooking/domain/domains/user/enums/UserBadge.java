package com.itcook.cooking.domain.domains.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserBadge {

    GIBBAB_GOSU("집밥 요리 전문가"),
    ;

    private final String description;
}
