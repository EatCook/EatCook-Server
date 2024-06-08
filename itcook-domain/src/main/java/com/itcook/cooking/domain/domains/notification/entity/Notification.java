package com.itcook.cooking.domain.domains.notification.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private Boolean checked = Boolean.FALSE;

    private Long userId;
    private Long postId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Builder
    public Notification(String title, String message, Long userId, Long postId
        ,NotificationType notificationType) {
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.postId = postId;
        this.notificationType = notificationType;
    }

    public static Notification createFollowAlarm(String subject, NotificationType notificationType,
        Long userId) {
        return Notification.builder()
            .title(notificationType.getTitle())
            .message(NotificationType.createMessage(subject, notificationType))
            .userId(userId)
            .build()
            ;
    }

    public static Notification createLikeAlarm(String subject, NotificationType notificationType,
        Long userId, Long postId) {
        return Notification.builder()
            .title(notificationType.getTitle())
            .postId(postId)
            .message(NotificationType.createMessage(subject, notificationType))
            .userId(userId)
            .build()
            ;
    }

    public void updateCheck() {
        checked = true;
    }


}
