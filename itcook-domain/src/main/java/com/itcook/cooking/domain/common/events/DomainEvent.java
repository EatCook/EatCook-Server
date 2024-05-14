package com.itcook.cooking.domain.common.events;

import java.time.LocalDateTime;

public class DomainEvent {
    private final LocalDateTime publishAt;

    public DomainEvent() {
        this.publishAt = LocalDateTime.now();
    }

}
