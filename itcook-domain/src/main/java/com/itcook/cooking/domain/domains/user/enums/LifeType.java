package com.itcook.cooking.domain.domains.user.enums;

import lombok.Getter;

@Getter
public enum LifeType {
    DIET("다이어트만 n번째"),
    HEALTH_DIET("건강한 식단관리"),
    CONVENIENCE_STORE("편의점은 내 구역"),
    DELIVERY_FOOD("배달음식 단골고객"),
    MEAL_KIT("밀키트 lover")

    ;


    private String lifeTypeName;

    LifeType(String lifeTypeName) {
        this.lifeTypeName = lifeTypeName;
    }

    public static LifeType getByName(String lifeTypeName) {
        for (LifeType lifeType : LifeType.values()) {
            if (lifeType.lifeTypeName.equals(lifeTypeName)) {
                return lifeType;
            }
        }
        throw new IllegalArgumentException("해당하는 생활 유형이 없습니다.");
    }
}
