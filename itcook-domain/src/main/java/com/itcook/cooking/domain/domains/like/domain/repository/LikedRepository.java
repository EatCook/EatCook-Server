package com.itcook.cooking.domain.domains.like.domain.repository;

import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    Optional<Liked> findByItCookUserIdAndPostId(Long itCookUserId, Long postId);

}
