package com.itcook.cooking.domain.domains.infra.email;

import com.itcook.cooking.domain.common.events.email.EmailSendEvent;

public interface AuthCodeService {

    void sentAuthCode(EmailSendEvent emailSendEvent);


}
