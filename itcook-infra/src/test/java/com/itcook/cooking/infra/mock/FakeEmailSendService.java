package com.itcook.cooking.infra.mock;

import com.itcook.cooking.infra.email.AuthCodeService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import lombok.RequiredArgsConstructor;

public class FakeEmailSendService implements AuthCodeService {

    public String subject;
    public String body;
    public String to;

    @Override
    public void sentAuthCode(EmailSendEvent emailSendEvent) {
        this.subject = emailSendEvent.getSubject();
        this.body = emailSendEvent.getBody();
        this.to = emailSendEvent.getTo();
    }
}
