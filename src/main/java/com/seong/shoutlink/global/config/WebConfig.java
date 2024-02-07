package com.seong.shoutlink.global.config;

import com.seong.shoutlink.global.auth.resolver.LoginUserArgumentResolver;
import com.seong.shoutlink.global.auth.authentication.AuthenticationContext;
import com.seong.shoutlink.global.auth.authentication.JwtAuthenticationInterceptor;
import com.seong.shoutlink.global.auth.authentication.JwtAuthenticationProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(authenticationContext));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:3000",
                "https://shoutlink.me",
                "https://www.shoutlink.me",
                "https://shout-link-front.vercel.app")
            .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
            .allowCredentials(true);
    }
}
