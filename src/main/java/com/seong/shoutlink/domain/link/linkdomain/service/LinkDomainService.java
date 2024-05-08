package com.seong.shoutlink.domain.link.linkdomain.service;

import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainLinksCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.UpdateLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainDetailResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainLinksResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.UpdateLinkDomainResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkPaginationResult;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainPaginationResult;
import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.util.DomainExtractor;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.service.LinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkDomainService implements LinkDomainUseCase {

    private final LinkDomainRepository linkDomainRepository;
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public UpdateLinkDomainResponse updateLinkDomain(UpdateLinkDomainCommand command) {
        String rootDomain = DomainExtractor.extractRootDomain(command.url());
        LinkDomain linkDomain = linkDomainRepository.findByRootDomain(rootDomain)
            .orElseGet(() -> {
                LinkDomain newLinkDomain = new LinkDomain(rootDomain);
                return linkDomainRepository.save(newLinkDomain);
            });
        Link link = linkRepository.findById(command.linkId())
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크입니다.", ErrorCode.NOT_FOUND));
        linkRepository.updateLinkDomain(link, linkDomain);
        return new UpdateLinkDomainResponse(linkDomain.getDomainId());
    }

    @Override
    public FindRootDomainsResponse findRootDomains(FindRootDomainsCommand command) {
        List<String> rootDomains = linkDomainRepository.findRootDomains(command.keyword(),
            command.size());
        return FindRootDomainsResponse.from(rootDomains);
    }

    @Override
    public FindLinkDomainsResponse findLinkDomains(FindLinkDomainsCommand command) {
        LinkDomainPaginationResult result = linkDomainRepository.findDomains(command.keyword(),
            command.page(), command.size());
        return FindLinkDomainsResponse.of(result.linkDomains(), result.totalElements(), result.hasNext());
    }

    @Override
    public FindLinkDomainDetailResponse findLinkDomain(FindLinkDomainCommand command) {
        LinkDomain linkDomain = getDomain(command.domainId());
        return FindLinkDomainDetailResponse.from(linkDomain);
    }

    @Override
    public FindLinkDomainLinksResponse findLinkDomainLinks(FindLinkDomainLinksCommand command) {
        LinkDomain linkDomain = getDomain(command.domainId());
        LinkDomainLinkPaginationResult result
            = linkDomainRepository.findDomainLinks(linkDomain, command.page(), command.size());
        return FindLinkDomainLinksResponse.of(result.links(), result.totalElements(), result.hasNext());
    }

    private LinkDomain getDomain(Long domainId) {
        return linkDomainRepository.findById(domainId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 도메인입니다.", ErrorCode.NOT_FOUND));
    }
}
