package com.seong.shoutlink.global.auth.authentication;

import com.seong.shoutlink.global.exception.ErrorCode;
import com.seong.shoutlink.global.exception.ShoutLinkException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private static final String HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationContext authenticationContext;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler) {
        log.debug("[Auth] JWT 인증 인터셉터 시작");
        String bearerAccessToken = request.getHeader(HEADER);
        if(Objects.nonNull(bearerAccessToken)) {
            log.debug("[Auth] JWT 인증 프로세스 시작");
            String accessToken = removeBearer(bearerAccessToken);
            JwtAuthentication authentication = jwtAuthenticationProvider.authenticate(accessToken);
            authenticationContext.setAuthentication(authentication);
            log.debug("[Auth] JWT 인증 프로세스 종료. 사용자 인증됨. {}", authentication);
        }
        log.debug("[Auth] Jwt 인증 인터셉터 종료");
        return true;
    }

    private String removeBearer(String bearerAccessToken) {
        if(!bearerAccessToken.contains(BEARER)) {
            throw new ShoutLinkException("올바르지 않은 액세스 토큰 형식입니다.", ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return bearerAccessToken.replace(BEARER, "");
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex) {
        authenticationContext.releaseContext();
    }
}
