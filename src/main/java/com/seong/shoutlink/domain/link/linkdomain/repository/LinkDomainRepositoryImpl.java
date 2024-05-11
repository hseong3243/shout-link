package com.seong.shoutlink.domain.link.linkdomain.repository;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.service.LinkDomainRepository;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkPaginationResult;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkResult;
import com.seong.shoutlink.domain.link.link.repository.LinkJpaRepository;
import java.util.List;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainPaginationResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkDomainRepositoryImpl implements LinkDomainRepository {

    private final LinkDomainJpaRepository linkDomainJpaRepository;
    private final LinkDomainCacheRepository linkDomainCacheRepository;
    private final LinkJpaRepository linkJpaRepository;

    @Override
    public List<String> findRootDomains(String keyword, int size) {
        return linkDomainCacheRepository.findRootDomains(keyword, size);
    }

    public void synchronizeRootDomains() {
        linkDomainJpaRepository.findRootDomains().forEach(linkDomainCacheRepository::insert);
    }

    @Override
    public LinkDomainPaginationResult findDomains(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LinkDomainEntity> domains = linkDomainJpaRepository.findDomains(keyword, pageRequest);
        return new LinkDomainPaginationResult(
            domains.map(LinkDomainEntity::toDomain).toList(),
            domains.getTotalElements(),
            domains.hasNext());
    }

    @Override
    public Optional<LinkDomain> findById(Long domainId) {
        return linkDomainJpaRepository.findById(domainId)
            .map(LinkDomainEntity::toDomain);
    }

    @Override
    public LinkDomainLinkPaginationResult findDomainLinks(LinkDomain linkDomain, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LinkDomainLinkResult> domainLinks
            = linkJpaRepository.findDomainLinks(linkDomain.getDomainId(), pageRequest);
        return new LinkDomainLinkPaginationResult(
            domainLinks.getContent(),
            domainLinks.getTotalElements(),
            domainLinks.hasNext());
    }
}
