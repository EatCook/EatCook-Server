package com.itcook.cooking.api.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.archive.domain.dto.ArchivePost;
import lombok.Builder;

@Builder
public record MyPageArchivePostsResponse(
    Long postId,
    String postImagePath
) {

    public static MyPageArchivePostsResponse of(ArchivePost archivePost) {
        return MyPageArchivePostsResponse.builder()
            .postId(archivePost.getPostId())
            .postImagePath(archivePost.getPostImagePath())
            .build()
            ;
    }
}
