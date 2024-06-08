package com.itcook.cooking.domain.domains.like.repository;

import com.itcook.cooking.domain.domains.like.entity.Liked;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    List<Liked> findAllByPostIdIn(List<Long> postIdData);
    Optional<Liked> findByItCookUserIdAndPostId(Long itCookUserId, Long postId);

}
