package com.seong.shoutlink.domain.link.service.request;

import jakarta.annotation.Nullable;

public record FindHubLinksCommand(
    Long linkBundleId,
    Long hubId,
    @Nullable
    Long nullableMemberId,
    int page,
    int size) {

}
