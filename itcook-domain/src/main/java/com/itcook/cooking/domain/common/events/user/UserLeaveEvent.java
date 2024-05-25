package com.itcook.cooking.domain.common.events.user;

import lombok.Builder;

@Builder
public record UserLeaveEvent(
    String email
){

}
