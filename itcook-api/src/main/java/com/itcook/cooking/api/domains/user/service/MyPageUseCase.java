package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageArchivePostsResponse;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.archive.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.service.ArchiveService;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageAlertUpdate;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class MyPageUseCase {

    private final UserService userService;
    private final PostService postService;
    private final ArchiveService archiveService;


    /**
     * 비밀번호 변경
     */
    public void changePassword(MyPagePasswordServiceDto passwordServiceDto) {
        userService.changePassword(passwordServiceDto.toDomainService());
    }

    /**
     * 회원 탈퇴
     */
    public void leaveUser(String email) {
        userService.leaveUser(email);
    }

    public void updateProfile(MyPageUpdateProfile myPageUpdateProfile) {
        userService.updateProfile(myPageUpdateProfile.email(),
            myPageUpdateProfile.nickName());
    }

    /**
     * 마이 프로필 설정 변경(서비스 이용 알림, 이벤트 알림)
     */
    @CacheEvict(cacheNames = "mypage", key = "#email")
    public void updateMyPageSetUp(String email,
        MyPageAlertUpdate myPageAlertUpdate) {
        userService.updateMyPageSetUp(email, myPageAlertUpdate.serviceAlertType(),
            myPageAlertUpdate.eventAlertType());
    }

    /**
     * 관심요리 업데이트
     */
    @CacheEvict(cacheNames = "interestCook", key = "#email")
    public void updateInterestCook(
        String email,
        UserUpdateInterestCook userUpdateInterestCook
    ) {
        userService.updateInterestCook(email, userUpdateInterestCook.cookingTypes(),
            userUpdateInterestCook.lifeType());
    }

}
