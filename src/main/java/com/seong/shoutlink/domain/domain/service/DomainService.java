package com.seong.shoutlink.domain.domain.service;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.request.FindDomainCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainLinksCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.domain.service.response.FindDomainDetailResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainLinksResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.UpdateDomainResponse;
import com.seong.shoutlink.domain.domain.service.result.DomainLinkPaginationResult;
import com.seong.shoutlink.domain.domain.service.result.DomainPaginationResult;
import com.seong.shoutlink.domain.domain.util.DomainExtractor;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomainService {

    private final DomainRepository domainRepository;
    private final LinkRepository linkRepository;

    @Transactional
    public UpdateDomainResponse updateDomain(UpdateDomainCommand command) {
        String rootDomain = DomainExtractor.extractRootDomain(command.url());
        Domain domain = domainRepository.findByRootDomain(rootDomain)
            .orElseGet(() -> {
                Domain newDomain = new Domain(rootDomain);
                return domainRepository.save(newDomain);
            });
        Link link = linkRepository.findById(command.linkId())
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크입니다.", ErrorCode.NOT_FOUND));
        linkRepository.updateLinkDomain(link, domain);
        return new UpdateDomainResponse(domain.getDomainId());
    }

    public FindRootDomainsResponse findRootDomains(FindRootDomainsCommand command) {
        List<String> rootDomains = domainRepository.findRootDomains(command.keyword(),
            command.size());
        return FindRootDomainsResponse.from(rootDomains);
    }

    public FindDomainsResponse findDomains(FindDomainsCommand command) {
        DomainPaginationResult result = domainRepository.findDomains(command.keyword(),
            command.page(), command.size());
        return FindDomainsResponse.of(result.domains(), result.totalElements(), result.hasNext());
    }

    public FindDomainDetailResponse findDomain(FindDomainCommand command) {
        Domain domain = getDomain(command.domainId());
        return FindDomainDetailResponse.from(domain);
    }

    public FindDomainLinksResponse findDomainLinks(FindDomainLinksCommand command) {
        Domain domain = getDomain(command.domainId());
        DomainLinkPaginationResult result
            = domainRepository.findDomainLinks(domain, command.page(), command.size());
        return FindDomainLinksResponse.of(result.links(), result.totalElements(), result.hasNext());
    }

    private Domain getDomain(Long domainId) {
        return domainRepository.findById(domainId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 도메인입니다.", ErrorCode.NOT_FOUND));
    }
}
