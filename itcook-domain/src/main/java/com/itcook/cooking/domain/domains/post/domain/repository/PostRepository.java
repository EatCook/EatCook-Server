package com.itcook.cooking.domain.domains.post.domain.repository;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndPostFlag(Long postId, PostFlag postFlag);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.postFlag = 'DISABLED' where p.userId = :userId")
    void updatePostToDisabled(Long userId);
}
