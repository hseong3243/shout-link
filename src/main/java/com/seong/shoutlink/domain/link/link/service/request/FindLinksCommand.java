package com.seong.shoutlink.domain.link.link.service.request;

import com.seong.shoutlink.domain.link.link.service.LinkOrderBy;

public record FindLinksCommand(Long memberId, Long linkBundleId, int page, int size,
                               LinkOrderBy linkOrderBy) {

}
