package com.itcook.cooking.api.global.config;

import static com.itcook.cooking.api.global.consts.ItCookConstants.SWAGGER_PATTERNS;

import com.itcook.cooking.api.global.security.jwt.entrypoint.JwtAuthenticationEntryPoint;
import com.itcook.cooking.api.global.security.jwt.filter.JwtCheckFilter;
import com.itcook.cooking.api.global.security.jwt.filter.JwtLogoutHandler;
import com.itcook.cooking.api.global.security.jwt.filter.JwtLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCheckFilter jwtCheckFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .formLogin().disable()
                .httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
            .antMatchers(SWAGGER_PATTERNS).permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
            .antMatchers("/api/v1/users/**").permitAll()
            .antMatchers("/api/v1/emails/**").permitAll()
            .antMatchers("/api/v1/users/find/**").permitAll()
            .anyRequest().hasRole("USER");

        http.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        // todo
        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .accessDeniedHandler(accessDeniedEntryPoint);
        ;

        http.logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(jwtLogoutHandler)
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
        ;

        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }


}
