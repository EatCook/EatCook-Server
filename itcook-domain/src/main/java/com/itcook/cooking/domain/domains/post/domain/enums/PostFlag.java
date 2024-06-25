package com.itcook.cooking.domain.domains.post.domain.enums;

import lombok.Getter;

@Getter
public enum PostFlag {
    DISABLED("disabled"),
    ACTIVATE("activate");

    private String postFlagName;

    PostFlag(String postFlagName) {
        this.postFlagName = postFlagName;
    }
}