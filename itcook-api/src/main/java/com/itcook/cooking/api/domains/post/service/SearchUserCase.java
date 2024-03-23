package com.itcook.cooking.api.domains.post.service;


import static java.util.stream.Collectors.toList;

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
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
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

    /**
     * 검색 리스트 하나로 RecipeName, IngredientName을 모두 검색한다.
     */
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

    /**
     * 1. 검색 리스트를 RecipeName, IngredientName으로 나누어 검색한다. (여러개의 쿼리를 날리는게 아닌 하나의 쿼리로 한번에 검색)
     * 2. 쿼리한 결과를 가져와서, 메모리상에서 검색명에 따른 결과를 출력
     */
    public List<SearchResultResponse> searchV3(Long lastId, List<String> recipeNames, List<String> ingredientNames, Integer size) {
        List<SearchPostDto> searchPostDtos = postQuerydslRepository.findAllWithPagination(lastId, recipeNames, ingredientNames, size);
        List<SearchPostProcess> searchList = getSearchList(searchPostDtos);

        return CollectionUtils.isEmpty(ingredientNames)
            ? getSearchResultResponses(recipeNames, searchList, this::isRecipeNameContains)
            : getSearchResultResponses(ingredientNames, searchList, this::isFoodIngredientContains);
    }

    private boolean isRecipeNameContains(SearchPostProcess searchPostProcess, String name) {
        return searchPostProcess.getRecipeName().contains(name);
    }

    private boolean isFoodIngredientContains(SearchPostProcess searchPostProcess, String name) {
        return searchPostProcess.getFoodIngredients().stream().anyMatch(foodIngredient -> foodIngredient.contains(name));
    }

    private List<SearchResultResponse> getSearchResultResponses(List<String> names, List<SearchPostProcess> searchList, BiPredicate<SearchPostProcess, String> predicate) {
        return names.stream()
            .map(name -> {
                List<SearchPostProcess> filteredSearchList = searchList.stream()
                    .filter(searchPostProcess -> predicate.test(searchPostProcess, name))
                    .collect(toList());
                return SearchResultResponse.builder()
                    .name(name)
                    .searchResults(filteredSearchList)
                    .build();
            })
            .collect(toList());
    }





    /**
     * 1. 검색 리스트를 RecipeName, IngredientName으로 나누어 검색한다. (여러번의 쿼리를 날림)
     * 2. 쿼리한 결과를 가져와서, 메모리상에서 검색명에 따른 결과를 출력
     */
    public List<SearchResultResponse> searchV2(Long lastId, List<String> recipeNames, List<String> ingredientNames, Integer size) {
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
            .collect(toList());
    }

}
