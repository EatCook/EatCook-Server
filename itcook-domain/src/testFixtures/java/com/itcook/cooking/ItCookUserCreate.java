package com.itcook.cooking;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;

public class ItCookUserCreate {

    public static ItCookUser createUser() {
        return ItCookUser.builder()
                .email("test@gmail.com")
                .password("1234")
                .nickName("테스트")
                .build()
                ;
    }
}
