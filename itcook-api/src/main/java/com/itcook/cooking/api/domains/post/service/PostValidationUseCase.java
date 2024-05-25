package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessDto;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.archive.entity.Archive;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.infra.s3.ImageFileExtension;
import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.infra.s3.S3PresignedUrlService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostValidationUseCase {

    private final S3PresignedUrlService s3PresignedUrlService;

    //팔로우 여부
    public boolean getFollowingCheck(Long userId, List<Long> followingSet) {
        return followingSet.contains(userId);
    }

    //좋아요 여부
    public boolean getLikedValidation(List<Liked> findAllLikedData, Long userId) {
        return findAllLikedData.stream()
                .anyMatch(liked -> liked.getItCookUserId().equals(userId));
    }

    //보관함 여부
    public boolean getArchiveValidation(List<Archive> archiveList, Long userId) {
        return archiveList.stream()
                .anyMatch(archive -> archive.getItCookUserId().equals(userId));
    }

    //레시피 본문 검증 및 presigned Url 발행
    public ImageUrlDto getPostFileExtensionValidation(Long userId, Long postId, String fileExtension) {
        ImageFileExtension mainImageExtension = ImageFileExtension.fromFileExtension(fileExtension);

        return s3PresignedUrlService.forPost(userId, postId, mainImageExtension.getUploadExtension());
    }

    //레시피 조리 과정 검증 및 presigned Url 발행
    public ImageUrlDto getRecipeProcessFileExtensionValidation(Long userId, Long postId, RecipeProcessDto recipeProcessDto) {
        ImageFileExtension recipeProcessImageExtension = ImageFileExtension.fromFileExtension(recipeProcessDto.getFileExtension());
        String uploadExtension = recipeProcessImageExtension.getUploadExtension();
        return s3PresignedUrlService.forRecipeProcess(userId, postId, uploadExtension);
    }
}
