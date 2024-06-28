package com.itcook.cooking.domain.domains.user.domain.enums;

public enum ProviderType {

    COMMON("common"),
    KAKAO("kakao"),
    APPLE("apple"),
    ;

    private final String provider;

    ProviderType(String provider) {
        this.provider = provider;
    }
}
