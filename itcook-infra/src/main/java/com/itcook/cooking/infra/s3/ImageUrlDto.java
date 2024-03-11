package com.itcook.cooking.infra.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ImageUrlDto {

    // presignedUrl
    private final String url;
    // fileName
    private final String key;

    public static ImageUrlDto of(String url, String key) {
        return ImageUrlDto.builder()
            .key(key)
            .url(url)
            .build();
    }
}
