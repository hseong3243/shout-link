package com.seong.shoutlink.domain.link.linkbundle.service.request;

public record CreateHubLinkBundleCommand(
    Long hubId,
    Long memberId,
    String description,
    boolean isDefault) {

}
