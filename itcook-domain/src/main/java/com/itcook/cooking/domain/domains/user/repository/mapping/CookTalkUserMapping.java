package com.itcook.cooking.domain.domains.user.repository.mapping;

import java.util.List;

public interface CookTalkUserMapping {
    Long getId();

    String getNickName();

    List<Long> getFollowings();
}
