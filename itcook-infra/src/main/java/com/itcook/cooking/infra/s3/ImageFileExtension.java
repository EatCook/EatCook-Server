package com.itcook.cooking.infra.s3;

import lombok.Getter;

@Getter
public enum ImageFileExtension {
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png");

    ImageFileExtension(String uploadExtension) {
        this.uploadExtension = uploadExtension;
    }

    private final String uploadExtension;

    public static ImageFileExtension fromFileExtension(String fileExtension)  {
        for (ImageFileExtension value : values()) {
            if (value.uploadExtension.equalsIgnoreCase(fileExtension)) {
                return value;
            }
        }
        return null;
    }
}