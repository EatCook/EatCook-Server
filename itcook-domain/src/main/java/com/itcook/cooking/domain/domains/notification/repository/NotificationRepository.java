package com.itcook.cooking.domain.domains.notification.repository;

import com.itcook.cooking.domain.domains.notification.entity.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndCheckedOrderByIdDesc(Long userId, boolean checked);

}
