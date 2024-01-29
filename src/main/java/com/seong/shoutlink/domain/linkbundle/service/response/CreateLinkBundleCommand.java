package com.seong.shoutlink.domain.linkbundle.service.response;

public record CreateLinkBundleCommand(Long memberId, String description, boolean isDefault) {

}
