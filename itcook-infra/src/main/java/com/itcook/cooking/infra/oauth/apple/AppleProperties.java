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

    private String grantType;
    private String clientId;
    private String keyId;
    private String teamId;
    private String audience;
    private String privateKey;
}
