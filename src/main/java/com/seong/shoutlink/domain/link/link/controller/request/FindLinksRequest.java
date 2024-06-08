package com.seong.shoutlink.domain.link.link.controller.request;

import com.seong.shoutlink.domain.link.link.service.LinkOrderBy;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record FindLinksRequest(
    @NotNull(message = "링크 묶음 ID는 필수입니다.")
    Long linkBundleId,
    Integer page,
    Integer size,
    LinkOrderBy linkOrderBy) {

    public FindLinksRequest(Long linkBundleId, Integer page, Integer size, LinkOrderBy linkOrderBy) {
        this.linkBundleId = linkBundleId;
        this.page = Objects.isNull(page) ? 0 : page;
        this.size = Objects.isNull(size) ? 20 : size;
        this.linkOrderBy = linkOrderBy == null ? LinkOrderBy.CREATED_AT : linkOrderBy;
    }
}
