package com.itcook.cooking.api.domains.notification.controller.dto;

import java.util.List;

public record UnCheckedNotisRequest(
    List<Long> notificationIds
) {

}
