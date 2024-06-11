package com.itcook.cooking.domain.domains.post.repository.dto;

import com.querydsl.core.Tuple;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.itcook.cooking.domain.domains.archive.entity.QArchive.archive;

@Data
@NoArgsConstructor
public class HomePostWithArchiveDto {
    private Long itCookUserId;
    private Long postId;

    @Builder
    public HomePostWithArchiveDto(Long itCookUserId, Long postId) {
        this.itCookUserId = itCookUserId;
        this.postId = postId;
    }

    public static HomePostWithArchiveDto from(Tuple tuple) {
        return HomePostWithArchiveDto.builder()
                .itCookUserId(tuple.get(archive.itCookUserId))
                .postId(tuple.get(archive.postId))
                .build();
    }
}
