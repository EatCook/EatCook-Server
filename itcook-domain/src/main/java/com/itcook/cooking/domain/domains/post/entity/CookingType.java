package com.itcook.cooking.domain.domains.post.entity;


import lombok.Getter;

@Getter
public enum CookingType {

    KOREAN_FOOD("한식"),
    JAPANESE_FOOD("일식"),
    WESTERN_FOOD("양식"),
    CHINESE_FOOD("중식"),
    ;


    private String cookingTypeName;

    CookingType(String cookingTypeName) {
        this.cookingTypeName = cookingTypeName;
    }
}
