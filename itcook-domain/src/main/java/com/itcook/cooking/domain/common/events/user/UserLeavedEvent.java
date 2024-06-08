package com.itcook.cooking.domain.common.events.user;

import com.itcook.cooking.domain.common.events.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserLeavedEvent extends DomainEvent {

    private final String email;

    public static UserLeavedEvent of(String email) {
        return UserLeavedEvent.builder()
            .email(email)
            .build();
    }
}
