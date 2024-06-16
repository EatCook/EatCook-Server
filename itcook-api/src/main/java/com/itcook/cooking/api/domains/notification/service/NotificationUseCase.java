package com.itcook.cooking.api.domains.notification.service;

import com.itcook.cooking.api.domains.notification.service.dto.response.NotificationResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import com.itcook.cooking.domain.domains.notification.service.NotificationService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class NotificationUseCase {

    private final UserService userService;
    private final NotificationService notificationService;

    public void updateNotisCheck(List<Long> notificationId) {
        notificationService.updateNotisCheck(notificationId);
    }
}
