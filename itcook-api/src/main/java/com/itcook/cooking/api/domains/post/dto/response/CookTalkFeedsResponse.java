package com.itcook.cooking.api.domains.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
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
public class CookTalkFeedsResponse {

    private Long writerUserId;
    private String writerUserEmail;
    private String writerProfile;
    private String writerNickname;
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;
    private Long likeCounts;
    private Boolean likedCheck;
    private Boolean followCheck;

    public static Page<CookTalkFeedsResponse> fromCookTalkFeedDto(
            Long authUserid,
            Page<CookTalkFeedDto> cookTalkFeeds,
            ItCookUser findByUserEmail
    ) {
        List<CookTalkFeedsResponse> cookTalkFeedsResponseList = cookTalkFeeds
                .map(cf -> CookTalkFeedsResponse.builder()
                        .writerUserId(cf.getWriterUserId())
                        .writerUserEmail(cf.getWriterUserEmail())
                        .writerProfile(cf.getWriterProfile())
                        .writerNickname(cf.getWriterNickName())
                        .postId(cf.getPostId())
                        .postImagePath(cf.getPostImagePath())
                        .recipeName(cf.getRecipeName())
                        .introduction(cf.getIntroduction())
                        .lastModifiedAt(cf.getLastModifiedAt())
                        .likeCounts(cf.getLikeCounts())
                        .likedCheck(cf.getLikedCheck())
                        .followCheck(findByUserEmail.getFollow().contains(cf.getWriterUserId()) &&
                                !cf.getWriterUserId().equals(authUserid))
                        .build())
                .getContent();

        return new PageImpl<>(cookTalkFeedsResponseList, cookTalkFeeds.getPageable(), cookTalkFeeds.getTotalElements());
    }
}