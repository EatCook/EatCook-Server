package com.itcook.cooking.domain.domains.infra.s3;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Set<String> findUnsupportedExtensions(Set<String> fileExtensions) {
        Set<String> supportedExtensions = new HashSet<>();
        for (ImageFileExtension value : ImageFileExtension.values()) {
            supportedExtensions.add(value.name().toLowerCase());
        }

        return fileExtensions.stream()
                .filter(extension -> !supportedExtensions.contains(extension))
                .collect(Collectors.toSet());
    }


}