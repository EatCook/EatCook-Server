package com.itcook.cooking.domain.domains.user.adaptor;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdaptor {

    private final UserRepository userRepository;

    public ItCookUser queryUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public ItCookUser queryUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public  void checkDuplicateEmail(String email) {
        userRepository.findByEmail(email)
            .ifPresent(it -> {
                throw new ApiException(UserErrorCode.ALREADY_EXISTS_USER);
            });
    }

    public void checkDuplicateNickname(String nickname) {
        userRepository.findByNickName(nickname)
            .ifPresent(it -> {
                throw new ApiException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
            });
    }

    public ItCookUser saveUser(ItCookUser user) {
        return userRepository.save(user);
    }

}
