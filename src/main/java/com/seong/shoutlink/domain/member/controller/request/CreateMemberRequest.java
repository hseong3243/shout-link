package com.seong.shoutlink.domain.member.controller.request;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    String email,
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    String password,
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    String nickname) {

}
