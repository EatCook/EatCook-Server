package com.itcook.cooking.infra.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.itcook.cooking.domain.domains.user.domain.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.infra.fcm.FcmService;
import com.itcook.cooking.domain.domains.infra.fcm.dto.FcmSend;
import com.itcook.cooking.infra.fcm.converter.NotificationPutDataConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserAdaptor userAdaptor;
    private final List<NotificationPutDataConverter> notificationPutDataConverters;

    @Override
    public void sendMessageTo(FcmSend fcmSend) {
        ItCookUser itCookUser = userAdaptor.queryUserById(fcmSend.targetUserId());

        if (Objects.isNull(itCookUser.getDeviceToken())) {
            log.error("유저 아이디 : {}의 디바이스 토큰이 존재하지 않습니다.", itCookUser.getId());
            return;
        }

        Notification notification = Notification.builder()
            .setTitle(fcmSend.title())
            .setBody(fcmSend.body())
            .build();

        log.info("device Token : {}", itCookUser.getDeviceToken());
        Message message = Message.builder()
            .setToken(itCookUser.getDeviceToken())
            .setNotification(notification)
            .putAllData(getData(fcmSend))
            .build();

        try {
            firebaseMessaging.sendAsync(message).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("파이어베이스 메시징 실패 to user ID: {}", itCookUser.getId(), e);
            if (e.getCause() instanceof FirebaseMessagingException fme) {
                log.error("파이어베이스 메세징 error code: {}", fme.getErrorCode());
            }
        }
    }

    private Map<String, String> getData(FcmSend fcmSend) {
        Map<String, String> data = new HashMap<>();
        return notificationPutDataConverters.stream().filter(
            notificationPutDataConverter -> notificationPutDataConverter.isSupports(fcmSend.notificationType()))
            .map(notificationPutDataConverter -> notificationPutDataConverter.getData(fcmSend))
            .findFirst()
            .orElseGet(() -> {
                data.put("empty","");
                return data;
            });
    }
}
