package com.seong.shoutlink.domain.auth.controller;

import com.seong.shoutlink.domain.auth.controller.request.CreateMemberRequest;
import com.seong.shoutlink.domain.auth.controller.request.LoginRequest;
import com.seong.shoutlink.domain.auth.controller.response.LoginApiResponse;
import com.seong.shoutlink.domain.auth.service.AuthService;
import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final AuthService authService;

    @PostMapping("/members")
    public ResponseEntity<CreateMemberResponse> createMember(
        @Valid @RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = authService.createMember(new CreateMemberCommand(
            request.email(),
            request.password(),
            request.nickname()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse res) {
        LoginResponse response = authService.login(new LoginCommand(
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
