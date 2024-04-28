package com.seong.shoutlink.domain.link.service.response;

public record CreateLinkBundleCommand(Long memberId, String description, boolean isDefault) {

}
