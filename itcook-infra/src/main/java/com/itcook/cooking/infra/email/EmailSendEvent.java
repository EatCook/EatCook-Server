package com.itcook.cooking.infra.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@Builder
public class EmailSendEvent {

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
