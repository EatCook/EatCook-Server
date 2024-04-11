package com.itcook.cooking.api.domains.user.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.domains.user.dto.request.MyPageChangePasswordRequest;
import com.itcook.cooking.api.domains.user.dto.request.MyPageUpdateProfileRequest;
import com.itcook.cooking.api.domains.user.service.MyPageUseCase;
import com.itcook.cooking.api.global.config.SecurityConfig;
import com.itcook.cooking.api.global.config.WithItCookMockUser;
import com.itcook.cooking.api.global.security.jwt.filter.JwtCheckFilter;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MyPageController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtCheckFilter.class),
    }
)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MyPageUseCase myPageUseCase;

    @MockBean
    private UserDomainService userDomainService;

    @Test
    @WithItCookMockUser
    @DisplayName("프로필 편집 요청")
    void updateProfile() throws Exception {
        //given
        MyPageUpdateProfileRequest request = MyPageUpdateProfileRequest.builder()
            .nickName("잇쿡22")
            .build();

        //when

        //then
        mockMvc.perform(patch("/api/v1/mypage/profile")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isOk())
        ;
    }

    @Test
    @WithItCookMockUser
    @DisplayName("프로필 편집 요청, 필드를 생략했을시 에러 발생")
    void updateProfileNull() throws Exception {
        //given
        MyPageUpdateProfileRequest request = MyPageUpdateProfileRequest.builder()
            .build();

        //when

        //then
        mockMvc.perform(patch("/api/v1/mypage/profile")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.nickName").value("닉네임을 입력해야합니다."))
        ;
    }

    @Test
    @WithItCookMockUser
    @DisplayName("비밀번호 변경 요청")
    void changePassword() throws Exception {
        //given
        MyPageChangePasswordRequest request = MyPageChangePasswordRequest.builder()
            .currentPassword("cook12345")
            .newPassword("cook1234")
            .build();

        //when

        //then
        mockMvc.perform(patch("/api/v1/mypage/profile/password")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request))
        )
            .andDo(print())
            .andExpect(status().isOk())
            ;
    }


    @Test
    @WithItCookMockUser
    @DisplayName("비밀번호 변경 요청 NotBlank Error")
    void changePasswordValidError() throws Exception {
        //given
        MyPageChangePasswordRequest request = MyPageChangePasswordRequest.builder()
            .newPassword("cook1234")
            .build();

        //when

        //then
        mockMvc.perform(patch("/api/v1/mypage/profile/password")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request))
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.currentPassword").value("현재 비밀번호를 입력하지 않았습니다."))
            ;
    }

    /**
     * TODO 레디스 캐싱 설정
     * 
     */

}