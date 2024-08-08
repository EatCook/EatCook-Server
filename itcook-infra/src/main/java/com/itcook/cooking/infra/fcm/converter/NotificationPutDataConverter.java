package com.itcook.cooking.infra.fcm.converter;

import com.itcook.cooking.domain.domains.infra.fcm.dto.FcmSend;
import com.itcook.cooking.domain.domains.notification.domain.entity.NotificationType;
import java.util.Map;

public interface NotificationPutDataConverter {
    boolean isSupports(NotificationType notificationType);
    Map<String, String> getData(FcmSend fcmSend);
}
