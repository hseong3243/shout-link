package com.seong.shoutlink.domain.link.link.service.request;

import com.seong.shoutlink.domain.link.link.service.LinkOrderBy;
import jakarta.annotation.Nullable;

public record FindHubLinksCommand(
    Long linkBundleId,
    Long hubId,
    @Nullable
    Long nullableMemberId,
    int page,
    int size,
    LinkOrderBy linkOrderBy) {

}
