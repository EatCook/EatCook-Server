package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.service.dto.MyPageResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageUserCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;

    /**
     * 마이페이지 조회
     */
    public MyPageResponse getMyPage(String email) {
        MyPageUserDto myPageUserInfo = userDomainService.getMyPageInfo(email);
        List<PostWithLikedDto> posts = postDomainService.getPostsByUserId(
            myPageUserInfo.getUserId());

        return MyPageResponse.from(myPageUserInfo, posts);
    }


}
