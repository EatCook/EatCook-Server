package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItCookUserRepository extends JpaRepository<ItCookUser, Long> {
}
