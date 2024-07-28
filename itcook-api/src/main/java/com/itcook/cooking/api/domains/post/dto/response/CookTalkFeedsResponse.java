package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "cooktalk feeds response")
public class CookTalkFeedsResponse {

    private Long writerUserId;
    private String writerUserEmail;
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;
    private Long likeCounts;
    private Boolean likedCheck;
    private Boolean followCheck;

    public static Page<CookTalkFeedsResponse> fromCookTalkFeedDto(
            Page<CookTalkFeedDto> cookTalkFeeds,
            ItCookUser findByUserEmail
    ) {
        List<CookTalkFeedsResponse> cookTalkFeedsResponseList = cookTalkFeeds
                .map(cf -> CookTalkFeedsResponse.builder()
                        .writerUserId(cf.getWriterUserId())
                        .writerUserEmail(cf.getWriterUserEmail())
                        .postId(cf.getPostId())
                        .postImagePath(cf.getPostImagePath())
                        .recipeName(cf.getRecipeName())
                        .introduction(cf.getIntroduction())
                        .likeCounts(cf.getLikeCounts())
                        .likedCheck(cf.getLikedCheck())
                        .followCheck(findByUserEmail.getFollow().contains(cf.getWriterUserId()))
                        .build())
                .getContent();

        return new PageImpl<>(cookTalkFeedsResponseList, cookTalkFeeds.getPageable(), cookTalkFeeds.getTotalElements());
    }
}