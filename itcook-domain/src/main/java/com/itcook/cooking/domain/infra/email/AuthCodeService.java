package com.itcook.cooking.domain.infra.email;

public interface AuthCodeService {

    void sentAuthCode(EmailSendEvent emailSendEvent);


}
