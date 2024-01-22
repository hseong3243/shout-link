package com.seong.shoutlink.domain.auth.service.request;

public record CreateMemberCommand(String email, String password, String nickname) {

}
