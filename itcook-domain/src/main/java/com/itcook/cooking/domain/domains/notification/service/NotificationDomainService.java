package com.itcook.cooking.domain.domains.notification.service;

import com.itcook.cooking.domain.domains.notification.adapter.NotificationAdapter;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationDomainService {

    private final NotificationAdapter notificationAdapter;

    public List<Notification> findNotiUnchecked(Long userId) {
        return notificationAdapter.queryNotiByUserIdWithUnchecked(
            userId);
    }

}
