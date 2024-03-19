package com.seong.shoutlink.domain.domain.service;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.domain.service.response.UpdateDomainResponse;
import com.seong.shoutlink.domain.domain.util.DomainExtractor;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.service.LinkRepository;
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
}
