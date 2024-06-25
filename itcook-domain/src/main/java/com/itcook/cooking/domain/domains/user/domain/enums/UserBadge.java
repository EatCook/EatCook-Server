package com.itcook.cooking.domain.domains.user.domain.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserBadge {

    GIBBAB_FINAL("미쉐린 쉐프", 100),
    GIBBAB_FOURTH("고독한 미식가", 70),
    GIBBAB_THIRD("초보 칼잡이",50),
    GIBBAB_SECOND("요리 새싹", 30),
    GIBBAB_FIRST("요리 사망꾼",0),
    ;

    private final String description;
    private final int postCount;

}
