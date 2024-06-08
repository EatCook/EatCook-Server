package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.entity.dto.SignupDto;
import com.itcook.cooking.domain.domains.user.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeJdbcRepository;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdatePassword;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserCookingThemeRepository userCookingThemeRepository;
    private final UserCookingThemeJdbcRepository userCookingThemeJdbcRepository;
    private final UserValidator userValidator;
    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    public ItCookUser findUserByEmail(String email) {
        return userAdaptor.queryUserByEmail(email);
    }

    public void checkDuplicateMail(String email) {
        userAdaptor.checkDuplicateEmail(email);
    }

    public ItCookUser save(ItCookUser user) {
        return userAdaptor.saveUser(user);
    }

    /**
     * 재발급
     *
     */
    @Transactional
    public String issueTemporaryPassword(String email) {
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        String temporaryPassword = RandomCodeUtils.generateTemporaryPassword();
        user.changePassword(passwordEncoder.encode(temporaryPassword));
        return temporaryPassword;
    }

    /**
     *  비밀번호 변경
     */
    @Transactional
    public void changePassword(UserUpdatePassword userUpdatePassword) {
        String newEncodedPassword = passwordEncoder.encode(userUpdatePassword.newPassword());
        String rawCurrentPassword = userUpdatePassword.rawCurrentPassword();

        ItCookUser user = findUserByEmail(userUpdatePassword.email());
        user.changePassword(newEncodedPassword, rawCurrentPassword, userValidator);
    }

    @Transactional
    public ItCookUser signup(String email, String password) {
        ItCookUser user = ItCookUser.signup(SignupDto.of(email,password, ProviderType.COMMON), userValidator);
        return userAdaptor.saveUser(user);
    }

    @Transactional
    public ItCookUser addSignup(ItCookUser user, List<CookingType> cookingTypes) {
        ItCookUser findUser = userAdaptor.queryUserByEmail(user.getEmail());
        List<UserCookingTheme> userCookingThemes = findUser.addSignup(user.getNickName(),
            user.getLifeType(), cookingTypes, userValidator);
        userCookingThemeJdbcRepository.saveAll(userCookingThemes, findUser.getId());
        return findUser;
    }

    public MyPageUserDto getMyPageInfo(String email) {
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        long followerCounts = userAdaptor.getFollowerCounts(user.getId());
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
//        userAdaptor.saveUser(user); // 이벤트 발생을 위한 save 호출
    }


    /**
     * 마이 프로필 설정(서비스 이용 알림, 이벤트 알림 조회)
     */
    public MyPageSetUpResponse getMyPageSetUp(String email) {
        log.info("getMyPageSetUp 조회");
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        return MyPageSetUpResponse.of(user);
    }

    /**
     * 마이 프로필 설정 변경(서비스 이용 알림, 이벤트 알림)
     */
    @Transactional
    public void updateMyPageSetUp(String email,
        ServiceAlertType serviceAlertType,
        EventAlertType eventAlertType
    ) {
        log.info("updateMyPageSetUp");
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        user.updateAlertTypes(serviceAlertType,
            eventAlertType);
    }

    public ItCookUser fetchFindByUserId(Long userId) {
        return userAdaptor.queryUserById(userId);
    }

    /**
     * 캐싱 CacheEvict
     */
    @Transactional
    public void updateInterestCook(
        String email,
        List<CookingType> cookingTypes,
        LifeType lifeType
    ) {
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        userCookingThemeRepository.deleteAllByUserId(user.getId());
        List<UserCookingTheme> cookingThemes = user.updateInterestCook(
            lifeType, cookingTypes);
        userAdaptor.saveUser(user);
        userCookingThemeJdbcRepository.saveAll(cookingThemes, user.getId());
    }

    /**
     * 관심요리 조회
     */
    public UserReadInterestCookResponse getInterestCook(
        String email
    ) {
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        List<UserCookingTheme> cookingThemes = userCookingThemeRepository.findAllByUserId(
            user.getId());
        return UserReadInterestCookResponse.of(user, cookingThemes);
    }
}
