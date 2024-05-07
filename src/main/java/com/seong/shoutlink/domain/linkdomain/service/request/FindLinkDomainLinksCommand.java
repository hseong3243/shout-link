package com.seong.shoutlink.domain.linkdomain.service.request;

public record FindLinkDomainLinksCommand(Long domainId, int page, int size) {

}
