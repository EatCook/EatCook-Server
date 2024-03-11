package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.PostSearchRequest;
import com.itcook.cooking.api.domains.post.dto.response.PostSearchResponse;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.domains.post.dto.response.SearchResponse;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    private PostDomainService postDomainService;

    @Operation(summary = "검색 요청", description = "검색 요청 설명")
    @PostMapping("/v1/search")
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> search(
        @RequestBody @Valid PostSearchRequest postSearchRequest
    ) {
        List<SearchResponse> searchResponses = postDomainService.searchByRecipeNameOrIngredients(
            postSearchRequest.getLastId(),
            postSearchRequest.getSearchWords(),
            postSearchRequest.getSize());

        List<PostSearchResponse> postSearchResponses = searchResponses.stream().map(PostSearchResponse::of)
            .toList();

        return ResponseEntity.ok(ApiResponse.OK(postSearchResponses));
    }
}
