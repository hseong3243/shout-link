package com.seong.shoutlink.domain.auth.service.response;

public record LoginResponse(Long memberId, String accessToken, String refreshToken) {

}
