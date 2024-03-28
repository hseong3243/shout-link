package com.seong.shoutlink.domain.domain.service.request;

public record FindDomainLinksCommand(Long domainId, int page, int size) {

}
