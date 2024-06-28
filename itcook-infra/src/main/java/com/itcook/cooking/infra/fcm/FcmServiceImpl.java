package com.itcook.cooking.infra.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.itcook.cooking.domain.domains.user.domain.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.infra.fcm.FcmService;
import com.itcook.cooking.domain.domains.infra.fcm.dto.FcmSend;
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

        Message message = Message.builder()
            .setToken(itCookUser.getDeviceToken())
            .setNotification(notification)
            .build();

        try {
            firebaseMessaging.sendAsync(message).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("유저 아이디 : {}, 알림 보내기 실패 : {}", itCookUser.getId() ,e.getMessage());
        }

//        try {
//            firebaseMessaging.send(message); //sendAsync 변환
//        } catch (FirebaseMessagingException e) {
//            log.error("유저 아이디 : {}, 알림 보내기 실패 : {}", itCookUser.getId() ,e.getMessage());
//        }
    }
}
