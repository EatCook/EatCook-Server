package com.itcook.cooking.domain.domains.user.domain.repository.mapping;

import java.util.List;

public interface CookTalkUserMapping {
    Long getId();

    String getNickName();

    List<Long> getFollow();
}
