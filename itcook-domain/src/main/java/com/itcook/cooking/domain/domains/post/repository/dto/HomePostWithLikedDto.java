package com.itcook.cooking.domain.domains.post.repository.dto;

import com.querydsl.core.Tuple;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.itcook.cooking.domain.domains.archive.entity.QArchive.archive;
import static com.itcook.cooking.domain.domains.like.entity.QLiked.liked;

@Data
@NoArgsConstructor
public class HomePostWithLikedDto {
    private Long itCookUserId;
    private Long postId;

    @Builder
    public HomePostWithLikedDto(Long itCookUserId, Long postId) {
        this.itCookUserId = itCookUserId;
        this.postId = postId;
    }

    public static HomePostWithLikedDto from(Tuple tuple) {
        return HomePostWithLikedDto.builder()
                .itCookUserId(tuple.get(archive.itCookUserId))
                .postId(tuple.get(archive.postId))
                .build();
    }
}
