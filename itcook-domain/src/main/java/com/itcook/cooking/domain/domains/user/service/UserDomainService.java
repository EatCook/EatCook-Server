package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;

import java.util.List;

import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserCookingThemeRepository userCookingThemeRepository;

    public ItCookUser fetchFindByEmail(String email) {

        Optional<ItCookUser> findByUserData = userRepository.findByEmail(email);
        if (findByUserData.isEmpty()) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        return findByUserData.get();
    }

    public List<CookTalkUserMapping> fetchFindUserByIdIn(List<Long> userId) {

        List<CookTalkUserMapping> findUserData = userRepository.findByIdIn(userId);

        if (ObjectUtils.isEmpty(findUserData)) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        return findUserData;
    }

    @Transactional(readOnly = true)
    public void findUserByEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(it -> {
                    throw new ApiException(UserErrorCode.ALREADY_EXISTS_USER);
                });
    }

    @Transactional
    public ItCookUser registerUser(ItCookUser user) {
        return userRepository.save(user);
    }

    @Transactional
    public ItCookUser addSignup(ItCookUser user, List<CookingType> cookingTypes) {
        //닉네임 중복 체크
        checkDuplicatedNicName(user);
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
            UserCookingTheme cookingTheme = UserCookingTheme.createUserCookingTheme(user.getId(), cookingType);
            userCookingThemeRepository.save(cookingTheme);
        });
    }

    private ItCookUser updateNickNameAndLifeType(ItCookUser user) {
        ItCookUser itCookUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        itCookUser.updateNickNameAndLifeType(user.getNickName(), user.getLifeType());
        return itCookUser;
    }

    private void checkDuplicatedNicName(ItCookUser user) {
        userRepository.findByNickName(user.getNickName())
                .ifPresent(it -> {
                    throw new ApiException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
                });
    }
}
