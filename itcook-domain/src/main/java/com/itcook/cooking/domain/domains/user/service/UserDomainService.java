package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserRepository userRepository;

    public ItCookUser fetchFindByEmail(String email) {

        Optional<ItCookUser> findByUserData = userRepository.findByEmail(email);
        if (findByUserData.isEmpty()) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        return findByUserData.get();
    }

    public List<CookTalkUserMapping> fetchFindUserByIdIn(List<Long> userId) {

        List<CookTalkUserMapping> findUserAllData = userRepository.findByIdIn(userId);

        if (ObjectUtils.isEmpty(findUserAllData)) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        return findUserAllData;
    }
}
