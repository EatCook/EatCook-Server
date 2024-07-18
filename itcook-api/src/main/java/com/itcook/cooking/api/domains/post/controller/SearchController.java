package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.PostSearchRequest;
import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.api.domains.post.dto.search.SearchPostResponse;
import com.itcook.cooking.api.domains.post.service.SearchUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "access-token")
@Tag(name = "04. Search")
public class SearchController {

    private final SearchUseCase searchUseCase;

    @Operation(summary = "검색 요청", description = "검색 요청. 첫 검색바에서 요청시에는 ingredients값으로 보내야합니다. "
        + "검색 뷰 리스트에서 레시피 태그를 누를때 recipeNames으로 값을 보내야합니다")
    @PostMapping("/v1/posts/search")
    public ResponseEntity<ApiResponse<List<SearchPostResponse>>> search(
        @RequestBody @Valid PostSearchRequest postSearchRequest
    ) {
        List<SearchPostResponse> response = searchUseCase.searchV4(
            postSearchRequest.toServiceDto()
        );
        return ResponseEntity.ok(ApiResponse.OK(response));
    }

    @Operation(summary = "검색어 랭킹", description = "검색어 랭킹. rank는 현재 등수, rankChange는 2, -1과 같은 변화한"
        + " 등수값을 의미합니다. 등수가 변화하지 않았거나 새로 들어온 검색어는 default로 0을 가진다.")
    @GetMapping("/v1/posts/search/ranking")
    public ResponseEntity<ApiResponse<SearchRankResponse>> getRankingWords() {
        SearchRankResponse rankingWords = searchUseCase.getRankingWords();
        return ResponseEntity.ok(ApiResponse.OK(rankingWords));
    }
}
