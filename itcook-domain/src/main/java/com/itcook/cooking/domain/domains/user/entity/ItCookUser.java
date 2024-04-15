package com.itcook.cooking.domain.domains.user.entity;

import static com.itcook.cooking.domain.common.constant.UserConstant.*;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.common.constant.UserConstant;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import javax.swing.text.html.Option;
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
        Assert.hasText(email, "Email is Not Empty");
        Assert.isTrue(email.matches(EMAIL_REGEXP), "유효한 이메일 형식이 아닙니다");
//        Assert.hasText(password, "Password is Not Empty");
//        Assert.isTrue(password.matches(PASSWORD_REGEXP), "패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.");
        Assert.notNull(email, "UserRole is Not Null");
        Assert.notNull(email, "ProviderType is Not Null");

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
}
