package com.itcook.cooking.domain.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
import lombok.Builder;

@Builder
public record MyPageAlertUpdate(
    ServiceAlertType serviceAlertType,
    EventAlertType eventAlertType
) {

}
