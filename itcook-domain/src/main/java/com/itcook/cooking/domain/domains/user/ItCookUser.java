package com.itcook.cooking.domain.domains.user;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "itcook_user")
@Getter
public class ItCookUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickName;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

}
