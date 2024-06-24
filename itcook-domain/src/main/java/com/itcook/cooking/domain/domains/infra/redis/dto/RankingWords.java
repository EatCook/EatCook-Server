package com.itcook.cooking.domain.domains.infra.redis.dto;

import lombok.Builder;

@Builder
public record RankingWords(
    String searchWord,
    Long searchCount
) {

    public static RankingWords of(String searchWord, Long searchCount) {
        return RankingWords.builder()
            .searchWord(searchWord)
            .searchCount(searchCount)
            .build()
            ;
    }

}
