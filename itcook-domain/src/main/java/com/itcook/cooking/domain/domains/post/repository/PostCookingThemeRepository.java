package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.PostCookingTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostCookingThemeRepository extends JpaRepository<PostCookingTheme, Long> {
    List<PostCookingTheme> findAllByPost(Post post);

}
