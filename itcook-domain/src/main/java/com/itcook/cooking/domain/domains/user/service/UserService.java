package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.events.Events;
import com.itcook.cooking.domain.common.events.user.UserFollowedEvent;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.entity.UserImageRegisterService;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.AddSignupDomainResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.LoginDto;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.MyPageProfileImageResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.SignupDto;
import com.itcook.cooking.domain.domains.user.domain.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.domain.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.domain.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.domain.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdatePassword;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageUserInfoResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPageUserInfoResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserCookingThemeRepository userCookingThemeRepository;
    private final UserImageRegisterService userImageRegisterService;
    private final UserValidator userValidator;
    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    public ItCookUser findUserByEmail(String email) {
        return userAdaptor.queryUserByEmail(email);
    }

    public Long findIdByEmail(String email) {
        return userAdaptor.queryUserByEmail(email).getId();
    }

    public void checkDuplicateMail(String email) {
        userAdaptor.checkDuplicateEmail(email);
    }

    /**
     * 재발급
     */
    @Transactional
    public String issueTemporaryPassword(String email) {
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        String temporaryPassword = RandomCodeUtils.generateTemporaryPassword();
        log.info("임시 비밀번호 : {}", temporaryPassword);
        user.issueTemporaryPassword(passwordEncoder.encode(temporaryPassword), temporaryPassword,
                email);
        return temporaryPassword;
    }

    @Transactional
    public void login(ItCookUser user) {
        ItCookUser findUser = userAdaptor.queryActiveUserByEmail(user.getEmail());
        findUser.login(LoginDto.of(user), userValidator);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(UserUpdatePassword userUpdatePassword) {
        String newEncodedPassword = passwordEncoder.encode(userUpdatePassword.newPassword());
        String rawCurrentPassword = userUpdatePassword.rawCurrentPassword();

        ItCookUser user = findUserByEmail(userUpdatePassword.email());
        user.changePassword(newEncodedPassword, rawCurrentPassword, userValidator);
    }

    @Transactional
    public void changePassword(ItCookUser user) {
        ItCookUser findUser
                = userAdaptor.queryUserByEmail(user.getEmail());
        findUser.changePassword(passwordEncoder.encode(user.getPassword()));
    }

    @Transactional
    public ItCookUser signup(ItCookUser itCookUser) {
        ItCookUser user = ItCookUser.signup(
                SignupDto.of(itCookUser.getEmail(),
                        itCookUser.getNickName(),
                        passwordEncoder.encode(itCookUser.getPassword()),
                        itCookUser.getProviderType(),
                        itCookUser.getDeviceToken()
                ),
                userValidator);
        return userAdaptor.saveUser(user);
    }

    @Transactional
    public AddSignupDomainResponse addSignup(ItCookUser user, String fileExtension,
                                             List<CookingType> cookingTypes) {
        ItCookUser findUser = userAdaptor.queryUserByEmail(user.getEmail());
        return findUser.addSignup(user.getNickName(),
                user.getLifeType(), cookingTypes, fileExtension, userValidator,
                userImageRegisterService);
    }

    public MyPageUserInfoResponse getMyPageInfo(String email) {
        ItCookUser user = userAdaptor.queryUserByEmail(email);
        long followerCounts = userAdaptor.getFollowerCounts(user.getId());
        return MyPageUserInfoResponse.of(user, followerCounts);
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
        userCookingThemeRepository.deleteByUser(user);
        user.updateInterestCook(lifeType, cookingTypes);
    }

    /**
     * 관심요리 조회
     */
    public UserReadInterestCookResponse getInterestCook(
            String email
    ) {
        ItCookUser user = userAdaptor.queryJoinCookingThemesByEmail(email);
        return UserReadInterestCookResponse.of(user);
    }

    @Transactional
    public MyPageProfileImageResponse changeMyProfileImage(ItCookUser user, String fileExtension) {
        user = userAdaptor.queryUserByEmail(user.getEmail());
        return user.changeProfileImage(fileExtension, userImageRegisterService);
    }

    public void checkDuplicateNickName(ItCookUser user) {
        user.checkNickName(userValidator);
    }

    public OtherPageUserInfoResponse getOtherPageInfo(String email, Long otherUserId) {
        ItCookUser authUser = userAdaptor.queryUserByEmail(email);
        ItCookUser otherUser = userAdaptor.queryUserById(otherUserId);
        List<ItCookUser> follow = userAdaptor.getFollow(otherUser.getId());
        boolean followCheck = follow.contains(authUser);
        long postCounts = userAdaptor.getUserPostCounts(otherUser.getId());
        return OtherPageUserInfoResponse.of(otherUser, follow.size(), followCheck, postCounts);
    }

    /**
     * 팔로우 등록
     */
    public void follow(String fromUserEmail, Long toUserId) {
        ItCookUser fromUser = userAdaptor.queryUserByEmail(fromUserEmail);
        ItCookUser toUser = userAdaptor.queryUserById(toUserId);

        List<Long> follow = fromUser.getFollow();

        if (follow.contains(toUserId)) {
            throw new ApiException(UserErrorCode.ALREADY_FOLLOW_USER);
        }

        follow.add(toUser.getId());

        Events.raise(UserFollowedEvent.of(fromUser.getId(), fromUser.getNickName(), toUserId));
    }

    /**
     * 팔로우 취소
     */
    public void unFollow(String fromUserEmail, Long toUserId) {
        ItCookUser fromItCookUserData = userAdaptor.queryUserByEmail(fromUserEmail);
        ItCookUser toItCookUserData = userAdaptor.queryUserById(toUserId);

        List<Long> follow = fromItCookUserData.getFollow();

        if (!follow.contains(toUserId)) {
            throw new ApiException(UserErrorCode.ALREADY_UNFOLLOW_USER);
        }

        follow.remove(toItCookUserData.getId());

        fromItCookUserData.updateFollow(follow);
    }
}
