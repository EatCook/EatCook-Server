package com.itcook.cooking;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;

import java.util.List;
import java.util.stream.IntStream;

public class ItCookUserCreate {

    public static ItCookUser createUser() {
        return ItCookUser.builder()
                .email("test@gmail.com")
                .password("1234")
                .nickName("테스트")
                .build()
                ;
    }

    public static List<ItCookUser> createUsers() {

        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> {
                    return ItCookUser.builder()
                            .email("test" + i + "@gmail.com")
                            .password("1234")
                            .nickName("테스트" + i)
                            .build();
                })
                .toList();
    }
}
