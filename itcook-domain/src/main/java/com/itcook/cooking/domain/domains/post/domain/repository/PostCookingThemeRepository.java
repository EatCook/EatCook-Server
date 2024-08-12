package com.itcook.cooking.domain.domains.post.domain.repository;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCookingThemeRepository extends JpaRepository<PostCookingTheme, Long> {
    List<PostCookingTheme> findAllByPost(Post post);

    void deleteAllByIdIn(List<Long> postCookingThemeIds);
}
