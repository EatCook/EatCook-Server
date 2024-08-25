package com.itcook.cooking.domain.domains.archive.domain.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class ArchiveValidator {
    /**
     * 게시글 생성 검증
     */
    public void validateAdd(Archive archive) {
        Assert.notNull(archive.getItCookUserId(), "보관할 회원 정보가 누락되었습니다.");
        Assert.notNull(archive.getPostId(), "보관할 게시글 정보가 누락되었습니다.");
    }
}
