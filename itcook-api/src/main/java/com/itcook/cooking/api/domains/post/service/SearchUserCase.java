package com.itcook.cooking.api.domains.post.service;


import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.api.domains.post.dto.response.SearchResponse;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.infra.redis.RealTimeSearchWords;
import com.itcook.cooking.infra.redis.RedisService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchUserCase {

    private final PostDomainService postDomainService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService redisService;

    public List<SearchResponse> search(
        Long lastId, List<String> names, Integer size
    ) {
        List<Post> posts = postDomainService.searchByRecipeNameOrIngredients(lastId, names, size);
        eventPublisher.publishEvent(
            RealTimeSearchWords.builder()
                .searchWords(names)
                .build());
        return posts.stream().map(SearchResponse::of).toList();
    }

    public List<SearchRankResponse> getRankingWords() {
        Set<TypedTuple<Object>> rankingWords = redisService.getRankingWords();
        return rankingWords.stream().map(SearchRankResponse::of).toList();
    }

}
