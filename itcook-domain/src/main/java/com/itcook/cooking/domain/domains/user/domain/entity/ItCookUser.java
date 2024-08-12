package com.itcook.cooking.domain.domains.user.domain.entity;

import static com.itcook.cooking.domain.domains.infra.email.EmailTemplate.PASSWORD_EMAIL;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.common.events.Events;
import com.itcook.cooking.domain.common.events.email.EmailSendEvent;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.AddSignupDomainResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.LoginDto;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.MyPageProfileImageResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.SignupDto;
import com.itcook.cooking.domain.domains.user.domain.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.domain.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.domain.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.enums.UserState;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserBadge;
import com.itcook.cooking.domain.common.events.user.UserLeavedEvent;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "itcook_user")
public class ItCookUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserBadge badge = UserBadge.GIBBAB_FIRST;

    @Enumerated(EnumType.STRING)
    private ServiceAlertType serviceAlertType = ServiceAlertType.DISABLED;

    @Enumerated(EnumType.STRING)
    private EventAlertType eventAlertType = EventAlertType.DISABLED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    private String profile; //프로필 이미지

    @Enumerated(EnumType.STRING)
    private LifeType lifeType; // 거주 형태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserState userState = UserState.ACTIVE;

    @ElementCollection
    @CollectionTable(name = "follow", joinColumns = @JoinColumn(name = "from_user", referencedColumnName = "user_id"))
    @Column(name = "to_user")
    private List<Long> follow = new ArrayList<>();

    @Column(name = "device_token")
    private String deviceToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserCookingTheme> userCookingThemes = new ArrayList<>();

    @Builder
    private ItCookUser(String email, String password, String nickName, UserRole userRole,
        String profile, ProviderType providerType, LifeType lifeType, List<Long> follow
        , String deviceToken
    ) {

        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userRole = userRole;
        this.profile = profile;
        this.providerType = providerType;
        this.lifeType = lifeType;
        this.deviceToken = deviceToken;
        if (!CollectionUtils.isEmpty(follow)) {
            this.follow.addAll(follow);
        }
    }

    // 회원가입 유저 생성
    public static ItCookUser signup(SignupDto signupDto, UserValidator userValidator) {
        ItCookUser user = ItCookUser.builder()
            .email(signupDto.email())
            .password(signupDto.password())
            .providerType(signupDto.providerType())
            .nickName(signupDto.nickName())
            .userRole(UserRole.USER)
            .deviceToken(signupDto.deviceToken())
            .build();
        userValidator.validateSignup(user);
        return user;
    }

    // 회원탈퇴
    public void delete() {
        String deleteEmail = email;
        userState = UserState.DELETE;
        this.email = null;
        profile = null;
        nickName = "탈퇴한 유저";
        serviceAlertType = ServiceAlertType.DISABLED;
        eventAlertType = EventAlertType.DISABLED;

        Events.raise(UserLeavedEvent.of(deleteEmail));
    }

    // 추가 회원가입
    public AddSignupDomainResponse addSignup(
        String nickName, LifeType lifeType, List<CookingType> cookingTypes,
        String fileExtension,
        UserValidator userValidator,
        UserImageRegisterService userImageRegisterService
    ) {

        userValidator.validateDuplicateNickName(nickName);
        this.nickName = nickName;
        this.lifeType = lifeType;
        addUserCookingThemes(cookingTypes);
        ImageUrlDto imageUrlDto = userImageRegisterService.getImageUrlDto(fileExtension, this);
        profile = imageUrlDto.getKey();
        return AddSignupDomainResponse.of(imageUrlDto.getUrl());
    }

    // 도메인 주도 방식의 관심요리 업데이트
    public void updateInterestCook(
        LifeType lifeType,
        List<CookingType> cookingTypes
    ) {
        updateLifeType(lifeType);
        addUserCookingThemes(cookingTypes);
    }

    public void login(LoginDto loginDto,UserValidator userValidator) {
        userValidator.validateCurrentPassword(this, loginDto.password());
        deviceToken = loginDto.deviceToken();
    }

    public void changePassword(String newEncodedPassword, String rawCurrentPassword,
        UserValidator userValidator) {
        userValidator.validateCurrentPassword(this, rawCurrentPassword);
        password = newEncodedPassword;
    }

    public void changePassword(String newEncodedPassword) {
        password = newEncodedPassword;
    }

    public void issueTemporaryPassword(String newEncodedTemporaryPassword,
        String temporaryPassword,
        String toEmail
    ) {

        password = newEncodedTemporaryPassword;
        Events.raise(
            EmailSendEvent.of(PASSWORD_EMAIL.getSub(), PASSWORD_EMAIL.formatBody(temporaryPassword),
                toEmail));
    }

    public void addFollowing(Long userId) {
        getFollow().add(userId);
    }

    public Long getFollowingCounts() {
        return (long) getFollow().size();
    }

    public String getBadgeName() {
        return getBadge().getDescription();
    }

    public void updateNickName(String newNickName, UserValidator userValidator) {
        userValidator.validateDuplicateNickName(newNickName);
        this.nickName = newNickName;
    }

    public void updateAlertTypes
        (
            ServiceAlertType serviceAlertType,
            EventAlertType eventAlertType
        ) {
        this.serviceAlertType = serviceAlertType;
        this.eventAlertType = eventAlertType;
    }

    public void updateFollow(List<Long> follow) {
        this.follow = follow;
    }

    public String getLifeTypeName() {
        if (lifeType == null) {
            return null;
        }
        return lifeType.getLifeTypeName();
    }

    public void changeDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public List<String> getCookingTypes() {
        return this.userCookingThemes.stream().map(UserCookingTheme::getCookingTypeName)
            .toList();
    }

    public void updateLifeType(
        LifeType lifeType
    ) {
        this.lifeType = lifeType;
    }

    public Boolean isAlim() {
        return serviceAlertType == ServiceAlertType.ACTIVATE;
    }

    public MyPageProfileImageResponse changeProfileImage(String fileExtension,
        UserImageRegisterService userImageRegisterService) {
        ImageUrlDto imageUrlDto = userImageRegisterService.getImageUrlDto(fileExtension, this);
        profile = imageUrlDto.getKey();
        return MyPageProfileImageResponse.from(imageUrlDto.getUrl());
    }

    public void addUserCookingThemes(List<CookingType> cookingTypes) {
        List<UserCookingTheme> userCookingThemes = UserCookingTheme.create(this, cookingTypes);
        this.userCookingThemes.clear();
        this.userCookingThemes.addAll(userCookingThemes);
    }

    public void checkNickName(UserValidator userValidator) {
        userValidator.validateDuplicateNickName(nickName);
    }
}
