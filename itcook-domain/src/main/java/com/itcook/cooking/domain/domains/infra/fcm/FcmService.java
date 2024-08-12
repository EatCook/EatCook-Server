package com.itcook.cooking.domain.domains.infra.fcm;

import com.itcook.cooking.domain.domains.infra.fcm.dto.FcmSend;

public interface FcmService {

    void sendMessageTo(FcmSend fcmSend);

}
