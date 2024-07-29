package com.itcook.cooking.domain.domains.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LifeType {
    DIET("다이어트만 n년째"),
    HEALTH_DIET("건강한 식단관리"),
    CONVENIENCE_STORE("편의점은 내 구역"),
    DELIVERY_FOOD("배달음식 단골고객"),
    MEAL_KIT("밀키트 lover"),
    EMPTY("없음")
    ;

    private final String lifeTypeName;

    public static LifeType getByName(String lifeTypeName) {
        for (LifeType lifeType : LifeType.values()) {
            if (lifeType.lifeTypeName.equals(lifeTypeName)) {
                return lifeType;
            }
        }
        return null;
    }

    public static LifeType getByLifeType(String lifeType) {
        for (LifeType value : LifeType.values()) {
            if (lifeType.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
}
