package com.itcook.cooking.domain.infra.fcm;

import com.itcook.cooking.domain.infra.fcm.dto.FcmSend;

public interface FcmService {

    void sendMessageTo(FcmSend fcmSend);

}
