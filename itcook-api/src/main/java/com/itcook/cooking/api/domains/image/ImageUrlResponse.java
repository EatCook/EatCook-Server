package com.itcook.cooking.api.domains.image;

import com.itcook.cooking.infra.s3.ImageUrlDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class ImageUrlResponse {

    private final String presignedUrl;
    private final String key;

    public static ImageUrlResponse of(ImageUrlDto imageUrlDto) {
        return ImageUrlResponse.builder()
            .presignedUrl(imageUrlDto.getUrl())
            .key(imageUrlDto.getKey())
            .build();
    }

}
