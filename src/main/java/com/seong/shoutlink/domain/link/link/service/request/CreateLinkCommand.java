package com.seong.shoutlink.domain.link.link.service.request;

import java.time.LocalDateTime;

public record CreateLinkCommand(Long memberId, Long linkBundleId, String url, String description,
                                LocalDateTime expiredAt) {

}
