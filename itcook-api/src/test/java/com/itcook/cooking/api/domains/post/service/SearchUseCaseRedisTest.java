package com.itcook.cooking.api.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.itcook.cooking.api.IntegrationRedisContainerSupport;
import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchUseCaseRedisTest extends IntegrationRedisContainerSupport {

    @Autowired
    private SearchUseCase searchUseCase;

    @Autowired
    private RedisService redisService;

    @AfterEach
    void tearDown() {
        redisService.deleteData("previousSearchWords");
        redisService.deleteData("searchWords");
    }

    @Test
    @DisplayName("빈 검색어 랭킹을 조회한다.")
    void getRankingWordsEmpty() {
        //given

        //when
        var response = searchUseCase.getRankingWords();

        //then
        assertThat(response.getRankings()).isEmpty();
    }

    @Test
    @DisplayName("랭킹이 변화 없는 기존의 들어있는 검색어 랭킹을 조회한다.")
    void getRankingWords() {
        //given
        redisService.incrementScore("searchWords", "된장", 1L);
        redisService.incrementScore("searchWords", "김치찌개", 2L);
        redisService.updateRankingChanges();

        //when
        var response = searchUseCase.getRankingWords();

        //then
        System.out.println(response.getLastUpdateTime());
        assertThat(response.getRankings()).hasSize(2)
            .extracting(SearchRankResponse.Ranking::getSearchWord, SearchRankResponse.Ranking::getSearchCount,
                SearchRankResponse.Ranking::getRank, SearchRankResponse.Ranking::getRankChange)
            .containsExactly(
                tuple("김치찌개", 2L, 1L, 0),
                tuple("된장", 1L, 2L, 0)
            )
        ;

    }

    @Test
    @DisplayName("랭킹의 변화가 있는 검색어 랭킹을 조회한다.")
    void getRankingWordsChange() {
        //given
        redisService.incrementScore("searchWords", "된장", 1L);
        redisService.incrementScore("searchWords", "김치찌개", 2L);
        redisService.updateRankingChanges();
        redisService.incrementScore("searchWords", "된장",2L);
        redisService.incrementScore("searchWords", "참치찌개",1L);
        redisService.updateRankingChanges();

        //when
        var response = searchUseCase.getRankingWords();

        //then
        assertThat(response.getRankings()).hasSize(3)
            .extracting(SearchRankResponse.Ranking::getSearchWord, SearchRankResponse.Ranking::getSearchCount,
                SearchRankResponse.Ranking::getRank, SearchRankResponse.Ranking::getRankChange)
            .containsExactly(
                tuple("된장", 3L, 1L, 1),
                tuple("김치찌개", 2L, 2L, -1),
                tuple("참치찌개", 1L, 3L, 0)
            )
        ;

    }

}
