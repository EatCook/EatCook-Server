package com.itcook.cooking.domain.common.events.user;

import lombok.Builder;

@Builder
public record UserLeavedEvent(
    String email
){

    public static UserLeavedEvent of(String email) {
        return UserLeavedEvent.builder()
            .email(email)
            .build();
    }
}
