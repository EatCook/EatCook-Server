package com.itcook.cooking.domain.domains.user.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.ResideType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
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

    @Column(nullable = false)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole = UserRole.USER;

    private String profile; //프로필 이미지

    private Integer likeCount; // 좋아요수

    @Enumerated(EnumType.STRING)
    private ResideType resideType; // 거주 형태

    @Enumerated(EnumType.STRING)
    private ProviderType providerType = ProviderType.COMMON;

    @ElementCollection
    @CollectionTable(name = "follower", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower_name")
    private List<String> followers;

    @ElementCollection
    @CollectionTable(name = "following", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following_name")
    private List<String> followings;

    @Builder
    public ItCookUser(Long id, String email, String password, String nickName, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userRole = userRole;
    }
}
