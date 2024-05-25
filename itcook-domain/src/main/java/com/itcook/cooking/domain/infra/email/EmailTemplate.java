package com.itcook.cooking.domain.infra.email;

import static com.itcook.cooking.domain.infra.email.EmailHtmlConstant.MAIL_BASIC_FORMAT;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    AUTH_EMAIL("잇쿡 이메일 인증 요청입니다.", MAIL_BASIC_FORMAT.formatted(
        "  <br>\n" +
            "  <br>\n" +
            "  <span style=\"font-size:18px;\">안녕하세요, 잇쿡 계정 생성을 환영합니다! <br>요청하신 인증코드는 아래와 같습니다 <br>\n" +
            "  <span style=\"font-size: 30px;color:#ff8c00;font-weight: bold\">%s</span>\n" +
            "  <br>\n" +
            "  <br>\n"
    )),
    PASSWORD_EMAIL("잇쿡 이메일 임시 비밀번호 발급 요청입니다.", MAIL_BASIC_FORMAT.formatted(
        "  <br>\n" +
            "  <br>\n" +
            "  <span style=\"font-size:18px;\">안녕하세요, 잇쿡 계정 생성을 환영합니다! <br>요청하신 임시 비밀번호는 아래와 같습니다 <br>\n" +
            "  <span style=\"font-size: 30px;color:#ff8c00;font-weight: bold\">%s</span>\n" +
            "  <br>\n" +
            "  <br>\n"
    ))
    ;


    private String sub;
    private String body;

    EmailTemplate(String sub, String body) {
        this.sub = sub;
        this.body = body;
    }

    public String formatBody(String authKey) {
        return body.formatted(authKey);
    }
}
