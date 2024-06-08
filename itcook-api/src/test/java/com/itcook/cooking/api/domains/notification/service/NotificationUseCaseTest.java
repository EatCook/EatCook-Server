package com.itcook.cooking.api.domains.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.notification.service.dto.response.NotificationResponse;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import com.itcook.cooking.domain.domains.notification.entity.NotificationType;
import com.itcook.cooking.domain.domains.notification.repository.NotificationRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NotificationUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private NotificationUseCase notificationUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("해당 유저의 알림이 있는지 조회한다.")
    void getNotifications() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        Notification noti1 = createNoti(user.getId(), 1);
        Notification noti2 = createNoti(user.getId(), 2);
        Notification noti3 = createNoti(user.getId(), 3);

        //when
        List<NotificationResponse> response = notificationUseCase.getNotifications(
            user.getEmail());


        //then
        assertThat(response).hasSize(3)
            .extracting("notificationId","checked")
            .containsExactly(
                tuple(noti3.getId(), false),
                tuple(noti2.getId(), false),
                tuple(noti1.getId(), false)
            );
    }
    @Test
    @DisplayName("해당 유저의 알림 목록이 없을경우 빈 리스트 반환")
    void getNotificationsEmpty() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        //when
        List<NotificationResponse> response = notificationUseCase.getNotifications(
            user.getEmail());

        //then
        assertThat(response).isEmpty();
    }

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    private Notification createNoti(Long userId, int i) {
        Notification notification = Notification.builder()
            .userId(userId)
            .title("알림 제목" + i)
            .message("알림 메시지")
            .notificationType(NotificationType.FOLLOW)
            .build();

        return notificationRepository.save(notification);
    }

}