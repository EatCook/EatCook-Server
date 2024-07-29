package com.itcook.cooking.domain.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtherPageUserInfoResponse {

    private Long userId;
    private String email;
    private String userImagePath;
    private String nickName;
    private String badge;
    private Integer followerCounts;
    private Boolean followCheck;
    private Long postCounts;

    @Builder
    public OtherPageUserInfoResponse(Long userId, String email, String userImagePath,
            String nickName,
            String badge, Integer followerCounts, Boolean followCheck, Long postCounts) {
        this.userId = userId;
        this.email = email;
        this.userImagePath = userImagePath;
        this.nickName = nickName;
        this.badge = badge;
        this.followerCounts = followerCounts;
        this.followCheck = followCheck;
        this.postCounts = postCounts;
    }

    public static OtherPageUserInfoResponse of(
            ItCookUser itCookUser, Integer followerCounts, Boolean followCheck,
            Long postCounts
    ) {
        return OtherPageUserInfoResponse.builder()
                .userId(itCookUser.getId())
                .email(itCookUser.getEmail())
                .userImagePath(itCookUser.getProfile())
                .nickName(itCookUser.getNickName())
                .badge(itCookUser.getBadgeName())
                .followerCounts(followerCounts)
                .followCheck(followCheck)
                .postCounts(postCounts)
                .build();

    }
}
