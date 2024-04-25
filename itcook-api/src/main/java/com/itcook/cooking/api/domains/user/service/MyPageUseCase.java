package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageUseCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 마이페이지 조회
     */
    public MyPageResponse getMyPage(String email, Pageable pageable) {
        MyPageUserDto myPageUserInfo = userDomainService.getMyPageInfo(email);
        Page<PostWithLikedDto> posts = postDomainService.getPostsByUserId(
            myPageUserInfo.getUserId(), pageable);

        return MyPageResponse.of(myPageUserInfo, PageResponse.of(posts));
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(MyPagePasswordServiceDto passwordServiceDto) {
        ItCookUser user = userDomainService.findUserByEmail(passwordServiceDto.getEmail());
        checkCurrentPassword(passwordServiceDto, user.getPassword());
        user.changePassword(passwordEncoder.encode(passwordServiceDto.getNewPassword()));
    }

    private void checkCurrentPassword(MyPagePasswordServiceDto passwordServiceDto,
        String userPassword) {
        String currentPassword = passwordServiceDto.getCurrentPassword();

        if (!passwordEncoder
            .matches(currentPassword, userPassword)) {
            throw new ApiException(UserErrorCode.NOT_EQUAL_PASSWORD);
        }
    }


}
