package com.itcook.cooking.api.global.config;

import static com.itcook.cooking.api.global.consts.ItCookConstants.SWAGGER_PATTERNS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.global.security.jwt.entrypoint.JwtAuthenticationEntryPoint;
import com.itcook.cooking.api.global.security.jwt.filter.JwtCheckFilter;
import com.itcook.cooking.api.global.security.jwt.filter.JwtLoginFilter;
import com.itcook.cooking.api.global.security.jwt.filter.JwtLogoutHandler;
import com.itcook.cooking.api.global.security.jwt.filter.JwtLogoutSuccessHandler;
import com.itcook.cooking.api.global.security.jwt.service.ItCookUserDetailsService;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final ItCookUserDetailsService userDetailsService;
    private final JwtCheckFilter jwtCheckFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().mvcMatchers(SWAGGER_PATTERNS);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
            .formLogin().disable()
            .httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
            .antMatchers(SWAGGER_PATTERNS).permitAll()
            .antMatchers("/open-api/**").permitAll()
            .antMatchers("/test").permitAll()
            .anyRequest().hasRole("USER");

        http.addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        // todo
        http.exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .accessDeniedHandler(accessDeniedEntryPoint);
        ;

        http.logout()
            .addLogoutHandler(jwtLogoutHandler)
            .logoutSuccessHandler(jwtLogoutSuccessHandler)
        ;

        return http.build();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(objectMapper,
            jwtTokenProvider);
        jwtLoginFilter.setAuthenticationManager(authenticationManager());
        return jwtLoginFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authenticationProvider);
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
