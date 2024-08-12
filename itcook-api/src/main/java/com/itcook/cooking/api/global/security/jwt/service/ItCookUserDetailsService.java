package com.itcook.cooking.api.global.security.jwt.service;

import static com.itcook.cooking.domain.common.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.itcook.cooking.api.domains.security.CommonUser;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.UserState;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItCookUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProviderUserService providerUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("로그인 시도");
        ItCookUser itCookUser = userRepository.findByEmailAndUserState(username, UserState.ACTIVE)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        CommonUser commonUser = CommonUser.of(itCookUser);

        return providerUserService.convert(commonUser);
    }
}
