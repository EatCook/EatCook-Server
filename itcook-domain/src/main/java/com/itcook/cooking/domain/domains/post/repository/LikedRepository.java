package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.repository.dto.LikedDomainDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    List<Liked> findAllByPostIdIn(List<Long> postIdData);
    Optional<Liked> findByItCookUserIdAndPostId(Long itCookUserId, Long postId);

}
