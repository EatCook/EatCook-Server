package com.itcook.cooking.infra.email;

import static org.junit.jupiter.api.Assertions.*;

import com.itcook.cooking.domain.domains.infra.email.AuthCodeService;
import com.itcook.cooking.domain.common.events.email.EmailSendEvent;
import com.itcook.cooking.infra.mock.FakeEmailSendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthCodeServiceTest {


    @Test
    @DisplayName("이메일 인증 코드 요청 서비스 테스트")
    void emailSendTest() {
        //given
        EmailSendEvent emailSendEvent = EmailSendEvent.builder()
            .subject("잇쿡 이메일 요청입니다.")
            .body("233423")
            .to("user1@naver.com")
            .build();

        //when
        AuthCodeService authCodeService = new FakeEmailSendService();
        authCodeService.sentAuthCode(emailSendEvent);

        //then
        assertEquals(emailSendEvent.getSubject(), ((FakeEmailSendService) authCodeService).subject);
        assertEquals(emailSendEvent.getBody(), ((FakeEmailSendService) authCodeService).body);
        assertEquals(emailSendEvent.getTo(), ((FakeEmailSendService) authCodeService).to);
    }


}