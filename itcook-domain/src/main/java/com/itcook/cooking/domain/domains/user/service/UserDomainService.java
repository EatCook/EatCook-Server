package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.checkDuplicateEmail;
import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.checkDuplicateNickname;
import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.findExistingUserByEmail;
import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.findExistingUserById;
import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.saveUser;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeJdbcRepository;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.repository.UserQueryRepository;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageAlertUpdate;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageLeaveUser;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import com.itcook.cooking.infra.redis.event.UserLeaveEvent;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserCookingThemeRepository userCookingThemeRepository;
    private final UserQueryRepository userQueryRepository;
    private final UserCookingThemeJdbcRepository userCookingThemeJdbcRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserValidator userValidator;

    public ItCookUser findUserByEmail(String email) {
        return findExistingUserByEmail(userRepository, email);
    }

    public void checkDuplicateMail(String email) {
        checkDuplicateEmail(userRepository, email);
    }

    @Transactional
    public ItCookUser signup(String email, String password) {
        ItCookUser user = ItCookUser.signup(email, password, userValidator);
        return saveUser(userRepository, user);
    }

    @Transactional
    public ItCookUser addSignup(ItCookUser user, List<CookingType> cookingTypes) {
        //닉네임 중복 체크
        checkDuplicateNickname(userRepository, user.getNickName());
        // 업데이트 필드 (닉네임, 거주형태)
        ItCookUser itCookUser = updateNickNameAndLifeType(user);

        createCookingThemes(user, cookingTypes);

        return itCookUser;
    }
    private ItCookUser updateNickNameAndLifeType(ItCookUser user) {
        ItCookUser itCookUser = findExistingUserById(userRepository, user.getId());
        itCookUser.updateNickNameAndLifeType(user.getNickName(), user.getLifeType());
        return itCookUser;
    }

    private void createCookingThemes(ItCookUser user, List<CookingType> cookingTypes) {
        if (CollectionUtils.isEmpty(cookingTypes)) {
            return;
        }

        List<UserCookingTheme> userCookingThemes = cookingTypes.stream()
            .map(cookingType ->
                UserCookingTheme.createUserCookingTheme(user.getId(), cookingType))
            .toList();
        userCookingThemeJdbcRepository.saveAll(userCookingThemes, user.getId());
    }


    public MyPageUserDto getMyPageInfo(String email) {
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        long followerCounts = userQueryRepository.getFollowerCounts(user.getId());
        return MyPageUserDto.of(user, followerCounts);
    }

    @Transactional
    public void updateProfile(MyPageUpdateProfile myPageUpdateProfile) {
        checkDuplicateNickname(userRepository, myPageUpdateProfile.nickName());
        ItCookUser user = findUserByEmail(myPageUpdateProfile.email());
        user.updateNickName(myPageUpdateProfile.nickName());
    }

    @Transactional
    public void leaveUser(MyPageLeaveUser myPageLeaveUser) {
        ItCookUser user = findExistingUserByEmail(userRepository, myPageLeaveUser.email());
        String existingEamil = user.getEmail();
        user.delete();
        // 해당 유저 엑세스 토큰 삭제
        eventPublisher.publishEvent(UserLeaveEvent.builder()
            .email(existingEamil)
            .build());
    }


    /**
     * 마이 프로필 설정(서비스 이용 알림, 이벤트 알림 조회)
     */
    @Cacheable(cacheNames = "mypage", key = "'user:'+'#email'")
    public MyPageSetUpResponse getMyPageSetUp(String email) {
        log.info("getMyPageSetUp 조회");
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        return MyPageSetUpResponse.of(user);
    }

    /**
     * 마이 프로필 설정 변경(서비스 이용 알림, 이벤트 알림)
     */
    @Transactional
    @CacheEvict(cacheNames = "mypage", key = "'user:'+'#email'")
    public void updateMyPageSetUp(String email,
        MyPageAlertUpdate myPageAlertUpdate) {

        log.info("updateMyPageSetUp");
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        user.updateAlertTypes(myPageAlertUpdate.serviceAlertType(),
            myPageAlertUpdate.eventAlertType());
    }

    public ItCookUser fetchFindByUserId(Long userId) {
        return findExistingUserById(userRepository, userId);
    }

    /**
     * 캐싱 CacheEvict
     */
    @Transactional
    @CacheEvict(cacheNames = "interestCook", key = "#email")
    public void updateInterestCook(
        String email,
        UserUpdateInterestCook userUpdateInterestCook
    ) {
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        user.updateLifeType(userUpdateInterestCook.lifeType());
        updateCookingThemes(userUpdateInterestCook, user);
    }

    private void updateCookingThemes(UserUpdateInterestCook userUpdateInterestCook, ItCookUser user) {
        userCookingThemeRepository.deleteAllByUserId(user.getId());
        createCookingThemes(user, userUpdateInterestCook.cookingTypes());
    }
    
    /**
     * 관심요리 조회
     */
    @Cacheable(cacheNames = "interestCook", key = "#email")
    public UserReadInterestCookResponse getInterestCook(
        String email
    ) {
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        List<UserCookingTheme> cookingThemes = userCookingThemeRepository.findAllByUserId(
            user.getId());
        return UserReadInterestCookResponse.of(user, cookingThemes);
    }
}
