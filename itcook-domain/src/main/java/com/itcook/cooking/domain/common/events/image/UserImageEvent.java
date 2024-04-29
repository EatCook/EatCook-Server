package com.itcook.cooking.domain.common.events.image;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class UserImageEvent {

    private final Long userId;
    private final String fileExtension;

}
