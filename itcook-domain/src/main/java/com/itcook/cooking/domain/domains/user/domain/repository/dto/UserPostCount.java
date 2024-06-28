package com.itcook.cooking.domain.domains.user.domain.repository.dto;

import com.itcook.cooking.domain.domains.user.domain.enums.UserBadge;
import java.util.Arrays;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPostCount {

    private Long userId;
    private Long postCount;

    @Builder
    public UserPostCount(Long userId, Long postCount) {
        this.userId = userId;
        this.postCount = postCount;
    }

    public UserBadge updateUserBadge() {
//        if (postCount < UserBadge.GIBBAB_SECOND.getPostCount()) return UserBadge.GIBBAB_FIRST;
//        if (postCount < UserBadge.GIBBAB_THIRD.getPostCount()) return UserBadge.GIBBAB_SECOND;
//        if (postCount < UserBadge.GIBBAB_FOURTH.getPostCount()) return UserBadge.GIBBAB_THIRD;
//        if (postCount < UserBadge.GIBBAB_FINAL.getPostCount()) return UserBadge.GIBBAB_FOURTH;
//        return UserBadge.GIBBAB_FINAL;

        return Arrays.stream(UserBadge.values())
            .filter(userBadge -> userBadge.getPostCount() <= postCount)
            .findFirst()
            .orElse(UserBadge.GIBBAB_FIRST)
            ;
    }
}
