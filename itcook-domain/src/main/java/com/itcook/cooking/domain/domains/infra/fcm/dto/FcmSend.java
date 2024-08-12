package com.itcook.cooking.domain.domains.infra.fcm.dto;

import com.itcook.cooking.domain.domains.notification.domain.entity.NotificationType;
import lombok.Builder;

@Builder
public record FcmSend(
    Long targetUserId,
    String title,
    String body,
    NotificationType notificationType,
    Long fromUserId,
    Long postId
) {

}
