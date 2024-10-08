package com.itcook.cooking.api.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.itcook.cooking.api.IntegrationRedisContainerSupport;
import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.domain.common.constant.SearchKeyConstant;
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
        redisService.deleteData(SearchKeyConstant.SEARCH_RANKING_KEY);
        redisService.deleteData(SearchKeyConstant.CACHED_RANKING_KEY);
        redisService.deleteData(SearchKeyConstant.LAST_UPDATE_KEY);
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
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "된장", 1L);
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "김치찌개", 2L);
        redisService.updateRankingChanges();

        //when
        var response = searchUseCase.getRankingWords();

        //then
        assertThat(response.getRankings()).hasSize(2)
            .extracting(SearchRankResponse.Ranking::getSearchWord, SearchRankResponse.Ranking::getSearchCount,
                SearchRankResponse.Ranking::getRank, SearchRankResponse.Ranking::getRankChange)
            .containsExactly(
                tuple("김치찌개", 2L, 1L, 1),
                tuple("된장", 1L, 2L, 0)
            )
        ;

    }

    @Test
    @DisplayName("랭킹의 변화가 있는 검색어 랭킹을 조회한다.")
    void getRankingWordsChange() {
        //given
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "된장", 1L);
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "김치찌개", 2L);
        redisService.updateRankingChanges();
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "된장",2L);
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "참치찌개",1L);
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

    @Test
    @DisplayName("현재 랭킹에는 변화가 있지만, 업데이트가 되지 않아 캐시된 리스트 반환한다.")
    void getRankingWordsChangeNotUpdate() {
        //given
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "된장", 1L);
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "김치찌개", 2L);
        redisService.updateRankingChanges();
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "된장",2L);
        redisService.incrementScore(SearchKeyConstant.SEARCH_RANKING_KEY, "참치찌개",1L);

        //when
        var response = searchUseCase.getRankingWords();

        //then
        assertThat(response.getRankings()).hasSize(2)
            .extracting(SearchRankResponse.Ranking::getSearchWord, SearchRankResponse.Ranking::getSearchCount,
                SearchRankResponse.Ranking::getRank, SearchRankResponse.Ranking::getRankChange)
            .containsExactly(
                tuple("김치찌개", 2L, 1L, 1),
                tuple("된장", 1L, 2L, 0)
            )
        ;

    }

}
