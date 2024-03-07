package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.repository.PostCookingThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostCookingThemeDomainService {

    private final PostCookingThemeRepository postCookingThemeRepository;
    public void createPostCookingTheme(List<PostCookingTheme> postCookingTheme) {
        postCookingThemeRepository.saveAll(postCookingTheme);
    }
    public List<PostCookingTheme> findAllPostCookingTheme(Post post) {
        List<PostCookingTheme> findAllPostCookingTheme = postCookingThemeRepository.findAllByPost(post);

        if (findAllPostCookingTheme.isEmpty()) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findAllPostCookingTheme;
    }
}
