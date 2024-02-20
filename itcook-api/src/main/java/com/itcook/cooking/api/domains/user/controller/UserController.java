package com.itcook.cooking.api.domains.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @GetMapping("/test")
    public String userVerifyTest() {
        log.info("테스트 userVerifyTest");
        return "테스트 성공";
    }

}
