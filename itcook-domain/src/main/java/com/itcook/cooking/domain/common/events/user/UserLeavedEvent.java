package com.itcook.cooking.domain.common.events.user;

import com.itcook.cooking.domain.common.events.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserLeavedEvent extends DomainEvent {

    private final Long userId;
    private final String email;

    public static UserLeavedEvent of(Long userId,String email) {
        return UserLeavedEvent.builder()
            .userId(userId)
            .email(email)
            .build();
    }
}
