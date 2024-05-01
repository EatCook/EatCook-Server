package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageAlertUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(name = "마이 페이지 프로필 설정 변경(알림) 요청")
public record MyPageAlertUpdateRequest(
    @NotNull(message = "서비스 알림 타입을 보내주세요")
    @Schema(description = "서비스 알림 타입(ACTIVATE,DISABLED)", example = "ACTIVATE")
    ServiceAlertType serviceAlertType,
    @NotNull(message = "이벤트 알림 타입을 보내주세요")
    @Schema(description = "이벤트 알림 타입(ACTIVATE,DISABLED)", example = "DISABLED")
    EventAlertType eventAlertType
) {

    public MyPageAlertUpdate toServiceDto() {
        return MyPageAlertUpdate.builder()
            .serviceAlertType(serviceAlertType)
            .eventAlertType(eventAlertType)
            .build();
    }

}
