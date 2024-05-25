package com.itcook.cooking.domain.domains.mock;

import com.itcook.cooking.domain.infra.email.AuthCodeService;
import com.itcook.cooking.domain.infra.email.EmailSendEvent;

public class MockEmailService implements AuthCodeService {

    @Override
    public void sentAuthCode(EmailSendEvent emailSendEvent) {

    }
}
