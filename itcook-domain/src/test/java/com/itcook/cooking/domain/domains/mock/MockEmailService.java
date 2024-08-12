package com.itcook.cooking.domain.domains.mock;

import com.itcook.cooking.domain.domains.infra.email.AuthCodeService;
import com.itcook.cooking.domain.common.events.email.EmailSendEvent;

public class MockEmailService implements AuthCodeService {

    @Override
    public void sentAuthCode(EmailSendEvent emailSendEvent) {

    }
}
