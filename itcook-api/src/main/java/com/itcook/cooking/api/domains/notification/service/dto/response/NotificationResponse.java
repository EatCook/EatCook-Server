package com.itcook.cooking.api.domains.notification.service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import java.util.List;
import lombok.Builder;

@Builder

public record NotificationResponse(
    String title,
    String message,
    @JsonInclude(Include.NON_NULL)
    Long postId
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
