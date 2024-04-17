package com.seong.shoutlink.domain.member.controller;

import com.seong.shoutlink.domain.member.controller.request.CreateMemberRequest;
import com.seong.shoutlink.domain.member.service.MemberUseCase;
import com.seong.shoutlink.domain.member.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.member.service.response.CreateMemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberUseCase memberUseCase;

    @PostMapping("/members")
    public ResponseEntity<CreateMemberResponse> createMember(
        @Valid @RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = memberUseCase.createMember(new CreateMemberCommand(
            request.email(),
            request.password(),
            request.nickname()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
