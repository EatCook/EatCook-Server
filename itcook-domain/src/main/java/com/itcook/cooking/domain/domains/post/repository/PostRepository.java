package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUserIdNotAndPostFlag(Long userId, PostFlag postFlag, Pageable pageable);

    Page<Post> findByUserIdInAndPostFlag(List<Long> userId, PostFlag postFlag, Pageable pageable);

    Optional<Post> findByIdAndPostFlag(Long postId, PostFlag postFlag);
}
