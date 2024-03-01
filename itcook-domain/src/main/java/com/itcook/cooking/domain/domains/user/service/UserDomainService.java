package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.CookingTheme;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.post.repository.CookingThemeRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;
    private final CookingThemeRepository cookingThemeRepository;

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
        cookingTypes.forEach(cookingType -> {
            CookingTheme cookingTheme = CookingTheme.createCookingTheme(user.getId(), cookingType);
            cookingThemeRepository.save(cookingTheme);
        });
    }

    private ItCookUser updateNickNameAndLifeType(ItCookUser user) {
        ItCookUser itCookUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        itCookUser.updateItCookUser(user);
        return itCookUser;
    }

    private void checkDuplicatedNicName(ItCookUser user) {
        userRepository.findByNickName(user.getNickName())
            .ifPresent(it -> {
                throw new ApiException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
            });
    }
}
