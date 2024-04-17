package com.seong.shoutlink.domain.member.service.request;

public record CreateMemberCommand(String email, String password, String nickname) {

}
