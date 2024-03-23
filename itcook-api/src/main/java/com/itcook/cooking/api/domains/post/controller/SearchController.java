package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.PostSearchRequest;
import com.itcook.cooking.api.domains.post.dto.response.SearchRankResponse;
import com.itcook.cooking.api.domains.post.dto.response.SearchResultResponse;
import com.itcook.cooking.api.domains.post.service.SearchUserCase;
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

    private final SearchUserCase searchUserCase;

    @Operation(summary = "검색 요청", description = "검색 요청 설명")
    @PostMapping("/v1/posts/search")
    public ResponseEntity<ApiResponse<List<SearchResultResponse>>> search(
        @RequestBody @Valid PostSearchRequest postSearchRequest
    ) {
        List<SearchResultResponse> searchResponses = searchUserCase.searchV3(
            postSearchRequest.getLastId(),
            postSearchRequest.getRecipeNames(),
            postSearchRequest.getIngredients(),
            postSearchRequest.getSize()
        );
        return ResponseEntity.ok(ApiResponse.OK(searchResponses));
    }

    @Operation(summary = "검색어 랭킹", description = "검색어 랭킹 설명")
    @GetMapping("/v1/posts/search/ranking")
    public ResponseEntity<ApiResponse<List<SearchRankResponse>>> getRankingWords() {
        List<SearchRankResponse> rankingWords = searchUserCase.getRankingWords();
        return ResponseEntity.ok(ApiResponse.OK(rankingWords));
    }
}
