package com.seong.shoutlink.domain.linkdomain.service;

import com.seong.shoutlink.domain.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.linkdomain.service.result.LinkDomainLinkPaginationResult;
import com.seong.shoutlink.domain.linkdomain.service.result.LinkDomainPaginationResult;
import java.util.List;
import java.util.Optional;

public interface LinkDomainRepository {

    Optional<LinkDomain> findByRootDomain(String rootDomain);

    LinkDomain save(LinkDomain linkDomain);

    List<String> findRootDomains(String keyword, int size);

    LinkDomainPaginationResult findDomains(String keyword, int page, int size);

    Optional<LinkDomain> findById(Long domainId);

    LinkDomainLinkPaginationResult findDomainLinks(LinkDomain linkDomain, int page, int size);
}
