package com.itcook.cooking.api.global.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithItCookUserSecurityContextFactory.class)
public @interface WithItCookMockUser {

}
