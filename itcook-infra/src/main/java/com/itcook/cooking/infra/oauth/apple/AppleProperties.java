package com.itcook.cooking.infra.oauth.apple;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.apple")
@ConstructorBinding
public class AppleProperties {

    private final String grantType;
    private final String clientId;
    private final String keyId;
    private final String teamId;
    private final String audience;
    private final String privateKey;
}
