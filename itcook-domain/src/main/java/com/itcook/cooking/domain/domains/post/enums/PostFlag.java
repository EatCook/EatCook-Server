package com.itcook.cooking.domain.domains.post.enums;

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