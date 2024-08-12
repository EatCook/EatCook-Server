package com.itcook.cooking.infra.redis.dto;

import lombok.Builder;

@Builder
public record RankingWords(
    String word,
    long score
) {

    public static RankingWords of(String word, long score) {
        return RankingWords.builder()
            .word(word)
            .score(score)
            .build();
    }
}
