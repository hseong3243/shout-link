package com.seong.shoutlink.domain.link.service.request;

public record CreateLinkCommand(Long memberId, String url, String description) {

}
