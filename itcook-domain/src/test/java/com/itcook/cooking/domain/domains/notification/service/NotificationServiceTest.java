package com.itcook.cooking.domain.domains.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import com.itcook.cooking.domain.domains.notification.entity.NotificationType;
import com.itcook.cooking.domain.domains.notification.repository.NotificationRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NotificationServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("모든 알림들을 조회한다.")
    void findUncheck() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");
        Notification notification1 = createNotification(user.getId());
        Notification notification2 = createNotification(user.getId());
        Notification notification3 = createNotificationCheck(user.getId());

        //when
        List<Notification> notiUnchecked = notificationService.findAllNoti(
            user.getId());

        //then
        assertThat(notiUnchecked).hasSize(3)
            .extracting("id")
            .containsExactly(notification3.getId(), notification2.getId(), notification1.getId());
    }

    @Test
    @DisplayName("알림이 없을 경우 빈 배열을 조회한다.")
    void findUncheckEmpty() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");

        //when
        List<Notification> notiUnchecked = notificationService.findAllNoti(
            user.getId());

        //then
        assertThat(notiUnchecked).isEmpty();
    }

    @Test
    @DisplayName("기존의 알림을 선택하여 , check로 업데이트 한다")
    void updateNotisCheck() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");
        Notification notification1 = createNotification(user.getId());
        Notification notification2 = createNotification(user.getId());
        Notification notification3 = createNotificationCheck(user.getId());

        List<Long> ids = Stream.of(notification3, notification2, notification1)
            .map(Notification::getId).toList();

        //when
        notificationService.updateNotisCheck(ids);

        //then
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(3)
            .extracting("checked")
            .containsExactly(true, true, true)
            ;
    }

    @Test
    @DisplayName("기존의 알림을 선택하여 , check로 업데이트 한다")
    void updateEmptyList() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");


        //when
        notificationService.updateNotisCheck(List.of());

        //then
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).isEmpty();
    }
    
    

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    private Notification createNotification(Long userId) {
        Notification noti = Notification.builder()
            .title("팔로우 요청")
            .message("팔로우 요청을 한다")
            .userId(userId)
            .notificationType(NotificationType.FOLLOW)
            .build();

        return notificationRepository.save(noti);
    }
    private Notification createNotificationCheck(Long userId) {
        Notification noti = Notification.builder()
            .title("팔로우 요청")
            .message("팔로우 요청을 한다")
            .userId(userId)
            .notificationType(NotificationType.FOLLOW)
            .build();
        noti.updateCheck();
        return notificationRepository.save(noti);
    }

}