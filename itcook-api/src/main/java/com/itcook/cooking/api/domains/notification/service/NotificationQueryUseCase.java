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
@Transactional(readOnly = true)
public class NotificationQueryUseCase {

    private final UserService userService;
    private final NotificationService notificationService;

    public List<NotificationResponse> getNotifications(String email) {
        ItCookUser user = userService.findUserByEmail(email);
        List<Notification> uncheckedNotis = notificationService.findAllNoti(
            user.getId());

        return NotificationResponse.of(uncheckedNotis);
    }
}
