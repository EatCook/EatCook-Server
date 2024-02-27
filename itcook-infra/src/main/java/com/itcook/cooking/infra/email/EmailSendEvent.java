package com.itcook.cooking.infra.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSendEvent {

    private String subject;
    private String body;
    private String to;
}
