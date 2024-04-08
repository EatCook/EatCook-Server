package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.domain.common.constant.UserConstant.PASSWORD_REGEXP;

import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.domain.domains.user.service.UserServiceHelper;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageUserCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 마이페이지 조회
     */
    public MyPageResponse getMyPage(String email) {
        MyPageUserDto myPageUserInfo = userDomainService.getMyPageInfo(email);
        List<PostWithLikedDto> posts = postDomainService.getPostsByUserId(
            myPageUserInfo.getUserId());

        return MyPageResponse.from(myPageUserInfo, posts);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(MyPagePasswordServiceDto passwordServiceDto) {
        ItCookUser user = UserServiceHelper.findExistingUserByEmail(userRepository,
            passwordServiceDto.getEmail());

        verifyPasswords(passwordServiceDto, user.getPassword());

        user.changePassword(passwordEncoder.encode(passwordServiceDto.getNewPassword()));
    }

    private void verifyPasswords(MyPagePasswordServiceDto passwordServiceDto, String userPassword) {
        String currentPassword = passwordServiceDto.getCurrentPassword();
        String newPassword = passwordServiceDto.getNewPassword();

        Assert.hasText(currentPassword, "현재 비밀번호가 입력되지 않았습니다.");
        Assert.hasText(newPassword, "새로운 비밀번호가 입력되지 않았습니다.");
        Assert.isTrue(newPassword.matches(PASSWORD_REGEXP), "패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.");

        if (!passwordEncoder
            .matches(currentPassword, userPassword)) {
            throw new ApiException(UserErrorCode.NOT_EQUAL_PASSWORD);
        }
    }


}
