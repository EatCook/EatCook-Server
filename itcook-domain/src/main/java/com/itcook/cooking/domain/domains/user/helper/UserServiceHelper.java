package com.itcook.cooking.domain.domains.user.helper;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceHelper {

    public static ItCookUser findExistingUserByEmail(UserRepository userRepository, String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public static ItCookUser findExistingUserById(UserRepository userRepository, Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public static void checkDuplicateEmail(UserRepository userRepository, String email) {
        userRepository.findByEmail(email)
                .ifPresent(it -> {
                    throw new ApiException(UserErrorCode.ALREADY_EXISTS_USER);
                });
    }

    public static void checkDuplicateNickname(UserRepository userRepository, String nickname) {
        userRepository.findByNickName(nickname)
                .ifPresent(it -> {
                    throw new ApiException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
                });
    }

    public static ItCookUser saveUser(UserRepository userRepository, ItCookUser user) {
        return userRepository.save(user);
    }

}
