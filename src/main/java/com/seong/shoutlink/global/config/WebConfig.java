package com.seong.shoutlink.global.config;

import com.seong.shoutlink.global.auth.authentication.AuthenticationContext;
import com.seong.shoutlink.global.auth.authentication.JwtAuthenticationInterceptor;
import com.seong.shoutlink.global.auth.authentication.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationContext authenticationContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
            new JwtAuthenticationInterceptor(jwtAuthenticationProvider, authenticationContext))
            .order(1)
            .addPathPatterns("/api/**");
    }
}
