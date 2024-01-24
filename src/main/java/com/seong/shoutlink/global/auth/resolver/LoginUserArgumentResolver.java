package com.seong.shoutlink.global.auth.resolver;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.global.auth.authentication.Authentication;
import com.seong.shoutlink.global.auth.authentication.AuthenticationContext;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationContext authenticationContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasLongParameterType = parameter.getParameterType().isAssignableFrom(Long.class);
        return hasParameterAnnotation && hasLongParameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = authenticationContext.getAuthentication();
        checkAuthenticated(authentication);
        return authentication.getPrincipal();
    }

    private void checkAuthenticated(Authentication authentication) {
        if(Objects.isNull(authentication)) {
            throw new ShoutLinkException("인증되지 않은 사용자 요청입니다.", ErrorCode.UNAUTHENTICATED);
        }
    }
}
