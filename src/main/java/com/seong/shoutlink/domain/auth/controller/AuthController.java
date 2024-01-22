package com.seong.shoutlink.domain.auth.controller;

import com.seong.shoutlink.domain.auth.controller.request.CreateMemberRequest;
import com.seong.shoutlink.domain.auth.service.AuthService;
import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
