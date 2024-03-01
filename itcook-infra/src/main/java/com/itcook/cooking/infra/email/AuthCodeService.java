package com.itcook.cooking.infra.email;

public interface AuthCodeService {

    void sentAuthCode(EmailSendEvent emailSendEvent);

//    void sendEmail(String subject, String content, String... to);

}
