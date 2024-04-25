package com.itcook.cooking.domain.domains.user.entity;

import static com.itcook.cooking.domain.common.constant.UserConstant.*;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.domains.user.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "itcook_user")
public class ItCookUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserBadge badge;

    @Enumerated(EnumType.STRING)
    private ServiceAlertType serviceAlertType;

    @Enumerated(EnumType.STRING)
    private EventAlertType eventAlertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    private String profile; //프로필 이미지

    @Enumerated(EnumType.STRING)
    private LifeType lifeType; // 거주 형태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType providerType;

    @ElementCollection
    @CollectionTable(name = "follow", joinColumns = @JoinColumn(name = "from_user", referencedColumnName = "user_id"))
    @Column(name = "to_user")
    private List<Long> follow;

    @Builder
    private ItCookUser(Long id, String email, String password, String nickName, UserRole userRole,
        String profile, ProviderType providerType, LifeType lifeType, List<Long> follow,
        ServiceAlertType serviceAlertType, EventAlertType eventAlertType
    ) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userRole = userRole;
        this.profile = profile;
        this.providerType = providerType;
        this.badge = UserBadge.GIBBAB_GOSU;
        this.follow = new ArrayList<>();
        this.lifeType = lifeType;
        this.serviceAlertType = ServiceAlertType.DISABLED;
        this.eventAlertType = EventAlertType.DISABLED;
    }

    // 회원가입 유저 생성
    public static ItCookUser signup(String email, String password, UserValidator userValidator) {
        ItCookUser user = ItCookUser.builder()
            .email(email)
            .password(password)
            .providerType(ProviderType.COMMON)
            .userRole(UserRole.USER)
            .build();
        userValidator.validateSignup(user);
        return user;
    }

    public void updateNickNameAndLifeType(String nickName, LifeType lifeType) {
        this.nickName = nickName;
        this.lifeType = lifeType;

    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
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

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
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

    public void updateLifeType(
        LifeType lifeType
    ) {
        this.lifeType = lifeType;
    }

}
