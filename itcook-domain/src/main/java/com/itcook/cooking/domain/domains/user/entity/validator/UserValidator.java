package com.itcook.cooking.domain.domains.user.entity.validator;

import static com.itcook.cooking.domain.common.constant.UserConstant.EMAIL_REGEXP;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UserValidator {


    public void validateSignup(ItCookUser user) {
        Assert.hasText(user.getEmail(), "이메일을 입력해야합니다.");
        Assert.isTrue(user.getEmail().matches(EMAIL_REGEXP), "유효한 이메일 형식이 아닙니다.");
        Assert.hasText(user.getPassword(), "비밀번호를 입력해야합니다.");
        Assert.notNull(user.getUserRole(), "UserRole is Not Null");
        Assert.notNull(user.getProviderType(), "ProviderType is Not Null");
    }

}
