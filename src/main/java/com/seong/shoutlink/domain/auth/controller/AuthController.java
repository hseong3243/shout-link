package com.seong.shoutlink.domain.auth.controller;

import com.seong.shoutlink.domain.auth.controller.request.LoginRequest;
import com.seong.shoutlink.domain.auth.controller.response.LoginApiResponse;
import com.seong.shoutlink.domain.auth.service.AuthUseCase;
import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse res) {
        LoginResponse response = authUseCase.login(new LoginCommand(
            request.email(),
            request.password()
        ));
        addRefreshTokenCookie(res, response);
        return ResponseEntity.ok(new LoginApiResponse(
            response.memberId(),
            response.accessToken()
        ));
    }

    private void addRefreshTokenCookie(
        HttpServletResponse response,
        LoginResponse loginResponse) {
        ResponseCookie refreshTokenCookie = ResponseCookie
            .from("refreshToken", loginResponse.refreshToken())
            .path("/api")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .domain(".shoutlink.me")
            .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
