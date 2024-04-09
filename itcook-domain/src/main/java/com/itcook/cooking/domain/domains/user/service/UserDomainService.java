package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.domains.user.service.UserServiceHelper.checkDuplicateEmail;
import static com.itcook.cooking.domain.domains.user.service.UserServiceHelper.checkDuplicateNickname;
import static com.itcook.cooking.domain.domains.user.service.UserServiceHelper.findExistingUserByEmail;
import static com.itcook.cooking.domain.domains.user.service.UserServiceHelper.findExistingUserById;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.repository.UserQueryRepository;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;

import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import java.util.List;

import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserCookingThemeRepository userCookingThemeRepository;
    private final UserQueryRepository userQueryRepository;

    public ItCookUser fetchFindByEmail(String email) {

        return findExistingUserByEmail(userRepository, email);

//        Optional<ItCookUser> findByUserData = userRepository.findByEmail(email);
//        if (findByUserData.isEmpty()) {
//            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
//        }
//
//        return findByUserData.get();
    }

    public List<CookTalkUserMapping> fetchFindUserByIdIn(List<Long> userId) {

        List<CookTalkUserMapping> findUserData = userRepository.findByIdIn(userId);

        if (ObjectUtils.isEmpty(findUserData)) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        return findUserData;
    }

    public void findUserByEmail(String email) {
        checkDuplicateEmail(userRepository, email);
    }

    @Transactional
    public ItCookUser registerUser(ItCookUser user) {
        return userRepository.save(user);
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

    private void createCookingThemes(ItCookUser user, List<CookingType> cookingTypes) {
        if (CollectionUtils.isEmpty(cookingTypes)) {
            return;
        }

        cookingTypes.forEach(cookingType -> {
            UserCookingTheme cookingTheme = UserCookingTheme.createUserCookingTheme(user.getId(),
                cookingType);
            userCookingThemeRepository.save(cookingTheme);
        });
    }

    private ItCookUser updateNickNameAndLifeType(ItCookUser user) {
        ItCookUser itCookUser = findExistingUserById(userRepository, user.getId());
        itCookUser.updateNickNameAndLifeType(user.getNickName(), user.getLifeType());
        return itCookUser;
    }


    public MyPageUserDto getMyPageInfo(String email) {
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        long followerCounts = userQueryRepository.getFollowerCounts(user.getId());
        return MyPageUserDto.from(user, followerCounts);
    }

    public String getCurrentPassword(String email) {
        ItCookUser user = findExistingUserByEmail(userRepository, email);
        return user.getPassword();
    }


    @Transactional
    public void updateProfile(MyPageUpdateProfile myPageUpdateProfile) {
        checkDuplicateNickname(userRepository, myPageUpdateProfile.nickName());
        ItCookUser user = findExistingUserByEmail(userRepository, myPageUpdateProfile.email());
        user.updateNickName(myPageUpdateProfile.nickName());
    }
}
