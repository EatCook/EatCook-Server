package com.itcook.cooking.infra.email;

public class EmailHtmlConstant {

    private static String HEADER_TITLE = "<span style=\"font-weight: bold;font-size: 48px;\">" +
        "<span style=\"color:#007bd9\">잇쿡</span>\n" +
        "</span>\n";
    private static String TAIL = "<hr>\n" +
        "<br>\n" +
        "<span style=\"font-size:12px;\"> " +
        "      <br>감사합니다. <br> 잇쿡 " +
        "</span>\n";

    public static String MAIL_BASIC_FORMAT = "<span style=\"font-family:Arial,sans-serif\">\n" +
        HEADER_TITLE +
        "%s" +
        TAIL +
        "</span>";


}
