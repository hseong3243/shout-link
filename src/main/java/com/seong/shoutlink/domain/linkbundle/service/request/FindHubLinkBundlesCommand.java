package com.seong.shoutlink.domain.linkbundle.service.request;

import jakarta.annotation.Nullable;

public record FindHubLinkBundlesCommand(Long hubId, @Nullable Long nullableMemberId) {

}
