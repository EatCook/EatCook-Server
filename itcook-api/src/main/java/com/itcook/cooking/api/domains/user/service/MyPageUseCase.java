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
import com.itcook.cooking.domain.domains.user.service.dto.MyPageAlertUpdate;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageLeaveUser;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import com.itcook.cooking.infra.redis.event.UserLeaveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

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

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void leaveUser(MyPageLeaveUser myPageLeaveUser) {
        userDomainService.leaveUser(myPageLeaveUser.email());
        // 해당 유저 엑세스 토큰 삭제
        eventPublisher.publishEvent(UserLeaveEvent.builder()
            .email(myPageLeaveUser.email())
            .build());
    }

    @Transactional
    public void updateProfile(MyPageUpdateProfile myPageUpdateProfile) {
        userDomainService.updateProfile(myPageUpdateProfile.email(),
            myPageUpdateProfile.nickName());
    }

    /**
     * 마이 프로필 설정 조회 (서비스 이용 알림, 이벤트 알림 조회)
     */
    @Cacheable(cacheNames = "mypage", key = "'user:'+'#email'")
    public MyPageSetUpResponse getMyPageSetUp(String email) {
        return userDomainService.getMyPageSetUp(email);
    }

    /**
     * 마이 프로필 설정 변경(서비스 이용 알림, 이벤트 알림)
     */
    @Transactional
    @CacheEvict(cacheNames = "mypage", key = "'user:'+'#email'")
    public void updateMyPageSetUp(String email,
        MyPageAlertUpdate myPageAlertUpdate) {
        userDomainService.updateMyPageSetUp(email, myPageAlertUpdate.serviceAlertType(),
            myPageAlertUpdate.eventAlertType());
    }

    /**
     * 관심요리 조회
     */
    @Cacheable(cacheNames = "interestCook", key = "#email")
    public UserReadInterestCookResponse getInterestCook(
        String email
    ) {
        return userDomainService.getInterestCook(email);
    }


    /**
     * 관심요리 업데이트
     */
    @Transactional
    @CacheEvict(cacheNames = "interestCook", key = "#email")
    public void updateInterestCook(
        String email,
        UserUpdateInterestCook userUpdateInterestCook
    ) {
        userDomainService.updateInterestCook(email, userUpdateInterestCook.cookingTypes(),
            userUpdateInterestCook.lifeType());
    }
}
