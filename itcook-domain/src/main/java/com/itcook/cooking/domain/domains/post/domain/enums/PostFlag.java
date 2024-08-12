package com.itcook.cooking.domain.domains.post.domain.enums;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import lombok.Getter;

@Getter
public enum PostFlag {
    DISABLED("disabled"),
    ACTIVATE("activate");

    private String postFlagName;

    PostFlag(String postFlagName) {
        this.postFlagName = postFlagName;
    }

    public static void checkDisablePostFlag(PostFlag flag) {
        if (flag.equals(DISABLED)) {
            throw new ApiException(PostErrorCode.POST_ALREADY_DISABLED);
        }
    }

}