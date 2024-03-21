package com.itcook.cooking.api.domains.post.service;


import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.api.domains.post.dto.response.SearchResultResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.api.domains.post.dto.response.SearchResponse;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.repository.PostQuerydslRepository;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.api.domains.post.dto.search.SearchPostProcess;
import com.itcook.cooking.domain.domains.post.repository.dto.SearchPostDto;
import com.itcook.cooking.domain.domains.post.service.PostServiceHelper;
import com.itcook.cooking.infra.redis.RealTimeSearchWords;
import com.itcook.cooking.infra.redis.RedisService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchUserCase {

    private final PostDomainService postDomainService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService redisService;
    private final PostRepository postRepository;
    private final PostQuerydslRepository postQuerydslRepository;

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

    public List<SearchPostProcess> findAllWithPagination(Long lastId, List<String> recipeNames, List<String> ingredientNames, Integer size) {
        List<SearchPostDto> searchPostDtos = postQuerydslRepository.findAllWithPagination(lastId, recipeNames, ingredientNames, size);

        return getSearchList(searchPostDtos);
    }

    public List<SearchResultResponse> search(Long lastId, List<String> recipeNames, List<String> ingredientNames, Integer size) {
        List<SearchResultResponse> searchResults = new ArrayList<>();

        if (CollectionUtils.isEmpty(recipeNames) && CollectionUtils.isEmpty(ingredientNames)) {
            searchResults.add(getIngredientSearchResult(lastId, null, null, size, "전체 검색"));
        } else {
            searchResults.addAll(getIngredientSearchResultsByNames(lastId, recipeNames, null, size));
            searchResults.addAll(getIngredientSearchResultsByNames(lastId, null, ingredientNames, size));
        }

        return searchResults;
    }

    private SearchResultResponse getIngredientSearchResult(Long lastId, String recipeName, String ingredientName, Integer size, String searchName) {
        List<SearchPostDto> searchPostDtos = postQuerydslRepository.findAllWithPagination2(lastId, recipeName, ingredientName, size);
        List<SearchPostProcess> searchPostProcesses = getSearchList(searchPostDtos);
        return new SearchResultResponse(searchName, searchPostProcesses);
    }

    private List<SearchResultResponse> getIngredientSearchResultsByNames(Long lastId, List<String> recipeNames, List<String> ingredientNames, Integer size) {
        List<SearchResultResponse> searchResults = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recipeNames)) {
            for (String recipe : recipeNames) {
                searchResults.add(getIngredientSearchResult(lastId, recipe, null, size, recipe));
            }
        }
        if (!CollectionUtils.isEmpty(ingredientNames)) {
            for (String ingredient : ingredientNames) {
                searchResults.add(getIngredientSearchResult(lastId, null, ingredient, size, ingredient));
            }
        }
        return searchResults;
    }

    private List<SearchPostProcess> getSearchList(List<SearchPostDto> searchPostDtos) {
        if (searchPostDtos.isEmpty()) {
            return List.of();
        }

        return searchPostDtos.stream()
            .map(searchPostDto -> {
                Post post = PostServiceHelper.findExistingPostByIdAndPostFlag(postRepository, searchPostDto.getPostId());
                return SearchPostProcess.from(searchPostDto, post.getFoodIngredients());
            })
            .collect(Collectors.toList());
    }

}
