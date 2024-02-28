package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CookTalkFacadeService {

    private final PostDomainService postService;
    private final UserDomainService userService;

    public List<CookTalkResponse> getCookTalk() {

        List<Post> postAllData = postService.findPostAllData();

        List<Long> userIdData = postAllData.stream().map(Post::getUserId).toList();

        List<CookTalkUserMapping> userAllData = userService.findUserAll(userIdData);

        return postAllData.stream()
                .flatMap(post -> userAllData.stream()
                        .filter(user -> post.getUserId().equals(user.getId()))
                        .map(user -> CookTalkResponse.of(post, user)))
                .toList();
    }

}
