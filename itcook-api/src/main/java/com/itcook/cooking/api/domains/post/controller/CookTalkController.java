package com.itcook.cooking.api.domains.post.controller;


import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.domains.post.service.CookTalkFacadeService;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cooktalk")
@RequiredArgsConstructor
@Slf4j
public class CookTalkController {

    private final CookTalkFacadeService cookTalkFacadeService;

    @GetMapping("/test")
    public String cookTalkVerifyTest() {
        log.info("테스트 cookTalkVerifyTest");
        return "/api/cooktalk/test 접근";
    }

    @Operation(summary = "cooktalk 요청", description = "")
    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<CookTalkResponse>>> retrieveAllCookTalk() {
        List<CookTalkResponse> cookTalkData = cookTalkFacadeService.getCookTalk();

        return ResponseEntity.ok(ApiResponse.OK(cookTalkData));
    }


}
