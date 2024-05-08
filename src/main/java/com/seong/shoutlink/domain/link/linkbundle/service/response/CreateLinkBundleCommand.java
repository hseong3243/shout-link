package com.seong.shoutlink.domain.link.linkbundle.service.response;

public record CreateLinkBundleCommand(Long memberId, String description, boolean isDefault) {

}
