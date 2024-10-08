package com.itcook.cooking.api.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import com.itcook.cooking.domain.domains.infra.redis.dto.WordsRanking;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchUseCaseMockTest extends IntegrationTestSupport {

    @Autowired
    private SearchUseCase searchUseCase;

    @Test
    @DisplayName("검색되지 않아 빈 랭킹을 가져온다")
    void getRankingWordsEmpty() {
        //given
        given(redisService.getRankingWords())
            .willReturn(WordsRanking.builder()
                .rankingWords(List.of())
                .build());

        //when
        SearchRankResponse rankingWords = searchUseCase.getRankingWords();

        //then
        assertThat(rankingWords.getRankings()).isEmpty();
    }

    @Test
    @DisplayName("검색 랭킹을 가져온다")
    void getRankingWords() {
        //given
        RankingWords rankingWords1 = rankingWords("김치찌개", 3L);
        RankingWords rankingWords2 = rankingWords("된장찌개", 2L);

        given(redisService.getRankingWords())
            .willReturn(WordsRanking.builder()
                .rankingWords(List.of(rankingWords1, rankingWords2))
                .build());

        //when
        SearchRankResponse rankingWords = searchUseCase.getRankingWords();

        //then
        assertThat(rankingWords.getRankings()).hasSize(2);
    }

    private RankingWords rankingWords(String searchWords, Long count) {
        return RankingWords.builder()
            .word(searchWords)
            .searchCount(count)
            .build();
    }
}
