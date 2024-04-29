package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.checkDuplicateEmail;
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
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserCookingThemeRepository userCookingThemeRepository;
    private final UserQueryRepository userQueryRepository;
    private final UserCookingThemeJdbcRepository userCookingThemeJdbcRepository;
    private final UserValidator userValidator;

    public ItCookUser findUserByEmail(String email) {
        return findExistingUserByEmail(userRepository, email);
    }

    public void checkDuplicateMail(String email) {
        checkDuplicateEmail(userRepository, email);
    }

    public ItCookUser save(ItCookUser user) {
        return saveUser(userRepository, user);
    }

    @Transactional
    public void changePassword(ItCookUser user,String password) {
        user.changePassword(password);
    }

    @Transactional
    public ItCookUser signup(String email, String password) {
        ItCookUser user = ItCookUser.signup(email, password, userValidator);
        return saveUser(userRepository, user);
    }

    @Transactional
    public ItCookUser addSignup(ItCookUser user, List<CookingType> cookingTypes) {
        ItCookUser findUser = findExistingUserByEmail(userRepository, user.getEmail());
        List<UserCookingTheme> userCookingThemes = findUser.addSignup(user.getNickName(),
            user.getLifeType(), cookingTypes, userValidator);
        userCookingThemeJdbcRepository.saveAll(userCookingThemes, findUser.getId());
        return findUser;
    }

    public MyPageUserDto getMyPageInfo(String email) {
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        long followerCounts = userQueryRepository.getFollowerCounts(user.getId());
        return MyPageUserDto.of(user, followerCounts);
    }

    @Transactional
    public void updateProfile(String email, String newNickName) {
        ItCookUser user = findUserByEmail(email);
        user.updateNickName(newNickName, userValidator);
    }

    @Transactional
    public void leaveUser(String email) {
        ItCookUser user = findUserByEmail(email);
        user.delete();
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
        userCookingThemeRepository.deleteAllByUserId(user.getId());
        List<UserCookingTheme> cookingThemes = user.updateInterestCook(
            userUpdateInterestCook.lifeType(), userUpdateInterestCook.cookingTypes());
        saveUser(userRepository, user);
        userCookingThemeJdbcRepository.saveAll(cookingThemes, user.getId());
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
