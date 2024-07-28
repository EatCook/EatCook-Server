package com.itcook.cooking.api.domains.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFollowDto;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "cooktalk feeds response")
public class CookTalkFollowsResponse {

    private Long writerUserId;
    private String writerUserEmail;
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;
    private Long likeCounts;
    private Boolean likedCheck;
    private Boolean followCheck;
    private LocalDateTime updatedAt;

    public static Page<CookTalkFollowsResponse> fromCookTalkFeedDto(
            Page<CookTalkFollowDto> cookTalkFeeds,
            ItCookUser findByUserEmail
    ) {
        List<CookTalkFollowsResponse> cookTalkFeedsResponseList = cookTalkFeeds
                .map(cf -> CookTalkFollowsResponse.builder()
                        .writerUserId(cf.getWriterUserId())
                        .writerUserEmail(cf.getWriterUserEmail())
                        .postId(cf.getPostId())
                        .postImagePath(cf.getPostImagePath())
                        .recipeName(cf.getRecipeName())
                        .introduction(cf.getIntroduction())
                        .lastModifiedAt(cf.getLastModifiedAt())
                        .likeCounts(cf.getLikeCounts())
                        .likedCheck(cf.getLikedCheck())
                        .followCheck(findByUserEmail.getFollow().contains(cf.getWriterUserId()))
                        .build())
                .getContent();

        return new PageImpl<>(cookTalkFeedsResponseList, cookTalkFeeds.getPageable(), cookTalkFeeds.getTotalElements());
    }
}
