package com.itcook.cooking.domain.domains.notification.repository;

import com.itcook.cooking.domain.domains.notification.entity.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByIdDesc(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update Notification n set n.checked = true where n.id in (:notificationIds)")
    void updateNotisChecked(List<Long> notificationIds);

}
