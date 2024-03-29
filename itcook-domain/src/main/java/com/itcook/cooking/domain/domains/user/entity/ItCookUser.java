package com.itcook.cooking.domain.domains.user.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.domains.user.enums.*;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String nickName;

    private String badge;

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
    public ItCookUser(Long id, String email, String password, String nickName, UserRole userRole,
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
        this.follow = follow;
        this.lifeType = lifeType;
        this.serviceAlertType = serviceAlertType;
        this.eventAlertType = eventAlertType;
    }

    public void updateNickNameAndLifeType(String nickName, LifeType lifeType) {
        this.nickName = nickName;
        this.lifeType = lifeType;

    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

}
