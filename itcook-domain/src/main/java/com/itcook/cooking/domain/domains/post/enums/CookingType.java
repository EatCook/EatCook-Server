package com.itcook.cooking.domain.domains.post.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CookingType {

    KOREAN_FOOD("한식"),
    JAPANESE_FOOD("일식"),
    WESTERN_FOOD("양식"),
    CHINESE_FOOD("중식"),
    SIDE_DISH("반찬"),
    LATE_NIGHT_SNACK("야식"),
    DESERT("디저트"),
    BUNSIK("분식"),
    ASIAN_FOOD("아시안"),
    ;


    private final String cookingTypeName;

    public static CookingType getByName(String cookingTypeName) {
        for (CookingType cookingType : CookingType.values()) {
            if (cookingType.cookingTypeName.equals(cookingTypeName)) {
                return cookingType;
            }
        }
        return null;
    }
}
