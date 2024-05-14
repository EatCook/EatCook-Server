package com.itcook.cooking.api.domains.notification.service;

import com.itcook.cooking.api.domains.notification.service.dto.response.NotificationResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import com.itcook.cooking.domain.domains.notification.service.NotificationDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationUseCase {

    private final UserDomainService userDomainService;
    private final NotificationDomainService notificationDomainService;

    public List<NotificationResponse> getNotifications(String email) {
        ItCookUser user = userDomainService.findUserByEmail(email);
        List<Notification> uncheckedNotis = notificationDomainService.findNotiUnchecked(
            user.getId());

        return NotificationResponse.of(uncheckedNotis);
    }

}
