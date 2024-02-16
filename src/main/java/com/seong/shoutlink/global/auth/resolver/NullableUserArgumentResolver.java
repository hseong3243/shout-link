package com.seong.shoutlink.global.auth.resolver;

import com.seong.shoutlink.domain.auth.NullableUser;
import com.seong.shoutlink.global.auth.authentication.Authentication;
import com.seong.shoutlink.global.auth.authentication.AuthenticationContext;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class NullableUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationContext authenticationContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(NullableUser.class);
        boolean hasLongParameterType = parameter.getParameterType().isAssignableFrom(Long.class);
        return hasParameterAnnotation && hasLongParameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            Authentication authentication = authenticationContext.getAuthentication();
            if(Objects.isNull(authentication)) {
                return null;
            }
            return authentication.getPrincipal();
    }
}
