package com.itcook.cooking;

import com.itcook.cooking.domain.domains.user.entity.User;

import java.util.List;
import java.util.stream.IntStream;

public class ItCookUserCreate {

    public static User createUser() {
        return User.builder()
                .email("test@gmail.com")
                .password("1234")
                .nickName("테스트")
                .build()
                ;
    }

    public static List<User> createUsers() {

        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> {
                    return User.builder()
                            .email("test" + i + "@gmail.com")
                            .password("1234")
                            .nickName("테스트" + i)
                            .build();
                })
                .toList();
    }
}
