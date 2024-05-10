package com.itcook.cooking.domain.domains.notification.adapter;

import com.itcook.cooking.domain.domains.notification.entity.Notification;
import com.itcook.cooking.domain.domains.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationAdapter {

    private final NotificationRepository notificationRepository;

    public List<Notification> queryNotiByUserIdWithUnchecked(Long userId) {
        return notificationRepository.findByUserIdAndChecked(userId, false)
            ;
    }

}
