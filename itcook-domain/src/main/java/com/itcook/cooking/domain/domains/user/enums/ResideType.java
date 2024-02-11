package com.itcook.cooking.domain.domains.user.enums;

public enum ResideType {
    ALONE("혼자 살아요"),
    WITH_PARENTS("부모님과 살아요"),
    WITH_FRIENDS("친구와 살아요"),
    HAVING_BABY("아이가 있어요"),
    WITH_SPOUSE("남편/아내와 살아요")

    ;


    private String resideTypeName;

    ResideType(String resideTypeName) {
        this.resideTypeName = resideTypeName;
    }
}
