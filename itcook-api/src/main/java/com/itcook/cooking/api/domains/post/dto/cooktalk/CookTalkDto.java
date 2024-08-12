package com.itcook.cooking.api.domains.post.dto.cooktalk;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CookTalkDto {

    private Long postId;
    private String introduction;
    private String postImagePath;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Long userId;
    private String nickName;
    private String profile;
    private Integer likeCount;

    private Boolean likedCheck;
    private Boolean followCheck;

    public CookTalkDto(Post data, ItCookUser user) {
        this.postId = data.getId();
        this.introduction = data.getIntroduction();
        this.postImagePath = data.getPostImagePath();
        this.createdAt = data.getCreatedAt();
        this.lastModifiedAt = data.getLastModifiedAt();
        this.userId = data.getUserId();
        this.nickName = user.getNickName();
        this.profile = user.getProfile();
    }


    public void likedInfoSet(Integer likedCount, Boolean likedCheck) {
        this.likeCount = likedCount;
        this.likedCheck = likedCheck;
    }

    public void followCheckSet(Boolean followCheck) {
        this.followCheck = followCheck;
    }

}
