package com.itcook.cooking.domain.common.events.email;

import com.itcook.cooking.domain.common.events.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@Builder
public class EmailSendEvent extends DomainEvent {

    private String subject;
    private String body;
    private String to;

    public static EmailSendEvent of(String subject, String body, String to) {
        return EmailSendEvent.builder()
            .subject(subject)
            .body(body)
            .to(to)
            .build();
    }
}
