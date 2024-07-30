package com.itcook.cooking.domain.domains.user.domain.adaptor;

import static com.itcook.cooking.domain.common.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.domain.enums.UserState;
import com.itcook.cooking.domain.domains.user.domain.repository.UserQueryRepository;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdaptor {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    public ItCookUser queryUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public ItCookUser queryUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public void checkDuplicateEmail(String email) {
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

    public long getFollowerCounts(Long userId) {
        return userQueryRepository.getFollowerCounts(userId);
    }

    public ItCookUser queryJoinCookingThemesByEmail(String email) {
        return userRepository.findItCookUserJoinCookingThemes(email);
    }

    public List<ItCookUser> getFollow(Long userId) {
        return userQueryRepository.getFollow(userId);
    }

    public long getUserPostCounts(Long userId) {
        return userQueryRepository.getUserPostCounts(userId);
    }

    public void queryActiveUserByEmail(String email) {
        userRepository.findByEmailAndUserState(email, UserState.ACTIVE)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
    }
}
