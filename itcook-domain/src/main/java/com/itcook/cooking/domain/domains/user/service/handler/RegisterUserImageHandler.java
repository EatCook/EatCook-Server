package com.itcook.cooking.domain.domains.user.service.handler;

import static com.itcook.cooking.domain.domains.user.helper.UserServiceHelper.findExistingUserById;

import com.itcook.cooking.domain.common.events.image.UserImageEvent;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.helper.UserServiceHelper;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.infra.s3.ImageFileExtension;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RegisterUserImageHandler {

    private final UserRepository userRepository;
    private final S3PresignedUrlService s3PresignedUrlService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleRegisterUserImage(UserImageEvent userImageEvent) {
        System.out.println("---------------RegisterUserImageHandler.handleRegisterUserImage");
        System.out.println("userImageEvent = " + userImageEvent);
//        ItCookUser user = findExistingUserById(userRepository,
//            userImageEvent.getUserId());
//        ImageFileExtension imageFileExtension = ImageFileExtension.fromFileExtension(
//            userImageEvent.getFileExtension());
//        ImageUrlDto imageUrlDto = s3PresignedUrlService.forUser(user.getId(),
//            imageFileExtension.getUploadExtension());
//        user.updateProfile(imageUrlDto.getKey());
    }

}
