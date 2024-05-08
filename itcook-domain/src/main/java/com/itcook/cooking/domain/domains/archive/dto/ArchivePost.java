package com.itcook.cooking.domain.domains.archive.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArchivePost {

    private Long postId;
    private String postImagePath;

    @Builder
    public ArchivePost(Long postId, String postImagePath) {
        this.postId = postId;
        this.postImagePath = postImagePath;
    }
}
