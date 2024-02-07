package com.itcook.cooking.domain.domains.user.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String nickName;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Builder
    public User(Long id, String email, String password, String nickName, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userRole = userRole;
    }
}
