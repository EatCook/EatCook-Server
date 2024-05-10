package com.itcook.cooking.api.domains.notification.service.dto.response;

import com.itcook.cooking.domain.domains.notification.entity.Notification;
import java.util.List;
import lombok.Builder;

@Builder
public record NotificationResponse(
    String title,
    String message
) {


    public static List<NotificationResponse> of(List<Notification> notification) {
        return notification.stream()
            .map(NotificationResponse::of)
            .toList();
    }
    private static NotificationResponse of(Notification notification) {
        return NotificationResponse.builder()
            .title(notification.getTitle())
            .message(notification.getMessage())
            .build();
    }

}
