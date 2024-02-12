package com.seong.shoutlink.domain.linkbundle.service.request;

public record CreateHubLinkBundleCommand(
    Long hubId,
    Long memberId,
    String description,
    boolean isDefault) {

}
