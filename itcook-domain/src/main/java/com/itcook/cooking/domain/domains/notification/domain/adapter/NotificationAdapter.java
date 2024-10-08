package com.itcook.cooking.domain.domains.notification.domain.adapter;

import static com.itcook.cooking.domain.common.errorcode.NotificationErrorCode.NOTIFICATION_NOT_FOUND;

import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.notification.domain.repository.NotificationRepository;
import com.itcook.cooking.domain.domains.notification.domain.entity.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationAdapter {

    private final NotificationRepository notificationRepository;

    public List<Notification> queryNotiByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByIdDesc(userId)
            ;
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ApiException(NOTIFICATION_NOT_FOUND))
            ;
    }

    public void updateNotisChecked(List<Long> notificationIds) {
        if (notificationIds.isEmpty()) {
            return;
        }
        notificationRepository.updateNotisChecked(notificationIds);
    }
}
