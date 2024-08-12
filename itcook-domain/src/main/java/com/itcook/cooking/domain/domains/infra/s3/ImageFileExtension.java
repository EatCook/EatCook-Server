package com.itcook.cooking.domain.domains.infra.s3;

import lombok.Getter;

@Getter
public enum ImageFileExtension {
    JPEG("jpeg"),
    JPG("jpeg"),
    PNG("png");

    ImageFileExtension(String uploadExtension) {
        this.uploadExtension = uploadExtension;
    }

    private final String uploadExtension;

    public static ImageFileExtension fromFileExtension(String fileExtension) {
        for (ImageFileExtension value : values()) {
            if (value.name().equalsIgnoreCase(fileExtension)) {
                return value;
            }
        }
        throw new IllegalArgumentException(fileExtension + "는 지원하지 않는 확장자입니다.");
    }

}