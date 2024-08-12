package com.itcook.cooking.domain.domains.user.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class ItCookUserTest {

    @Mock
    UserImageRegisterService userImageRegisterService;

    @Test
    @DisplayName("fileExtension null값을 받은 유저 프로필 이미지 업데이트")
    void changeProfileNull() {
        //given
        ItCookUser user = ItCookUser.builder()
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡")
            .profile("myProfile")
            .userRole(UserRole.USER)
            .build();

        when(userImageRegisterService.getImageUrlDto(null, user))
            .thenReturn(ImageUrlDto.builder().build());

        //when
        user.changeProfileImage(null, userImageRegisterService);

        //then
        assertThat(user.getProfile()).isNull();
    }
    @Test
    @DisplayName("fileExtension 입력을 받아 받은 유저 프로필 이미지 업데이트")
    void changeProfile() {
        //given
        ItCookUser user = ItCookUser.builder()
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡")
            .profile("myProfile")
            .userRole(UserRole.USER)
            .build();

        String key = "s3-profile-key";
        when(userImageRegisterService.getImageUrlDto("jpg", user))
            .thenReturn(ImageUrlDto.builder()
                .key(key)
                .build());

        //when
        user.changeProfileImage("jpg", userImageRegisterService);

        //then
        assertThat(user.getProfile()).isEqualTo(key);
    }

    @Test
    @DisplayName("유저의 알림이 유형에 따라 true false를 반환한다.")
    void isAlim() {
        //given
        ItCookUser user = ItCookUser.builder()
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡")
            .profile("myProfile")
            .userRole(UserRole.USER)
            .build();

        //when
        Boolean alim = user.isAlim();

        //then
        assertThat(alim).isFalse();
    }
}