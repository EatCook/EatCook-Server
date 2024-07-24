package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.service.dto.response.MyPageArchivePostsResponse;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.archive.domain.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.service.ArchiveService;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageUserInfoResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageQueryUseCase {

    private final UserService userService;
    private final PostService postService;
    private final ArchiveService archiveService;


    /**
     * 마이페이지 유저 정보 조회
     */
    public MyPageUserInfoResponse getMyPageUserInfo(String email) {
        return userService.getMyPageInfo(email);
    }

    /**
     * 마이페이지의 마이레시피 조회
     */
    public PageResponse<MyRecipeResponse> getMyPageMyRecipe(String email, Pageable pageable) {
        ItCookUser user = userService.findUserByEmail(email);
        Page<MyRecipeResponse> posts = postService.getPostsByUserId(user.getId(), pageable);
        return PageResponse.of(posts);
    }

    /**
     * 마이 프로필 설정 조회 (서비스 이용 알림, 이벤트 알림 조회)
     */
    @Cacheable(cacheNames = "mypage", key = "#email")
    public MyPageSetUpResponse getMyPageSetUp(String email) {
        return userService.getMyPageSetUp(email);
    }


    /**
     * 관심요리 조회
     */
    @Cacheable(cacheNames = "interestCook", key = "#email")
    public UserReadInterestCookResponse getInterestCook(
        String email
    ) {
        return userService.getInterestCook(email);
    }


    /**
     * 북마크 보관함 조회
     */
    public List<MyPageArchivePostsResponse> getArchivePosts(String email) {
        ItCookUser user = userService.findUserByEmail(email);
        List<ArchivePost> archivesPosts = archiveService.getArchivesPosts(user.getId());
        return archivesPosts.stream().map(MyPageArchivePostsResponse::of).toList();
    }


}
