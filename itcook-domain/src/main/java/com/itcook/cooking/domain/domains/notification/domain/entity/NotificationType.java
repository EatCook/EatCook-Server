package com.itcook.cooking.domain.domains.notification.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    FOLLOW("팔로우 요청","회원님을 팔로우 했습니다."),
    LIKE("좋아요 요청","회원님의 레시피를 좋아합니다."),
    ;
    private final String title;
    private final String message;

    static String createMessage(String subject, NotificationType notificationType) {
        return String.format("%s님이 %s", subject, notificationType.getMessage());
    }
}
