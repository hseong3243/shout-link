package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.event.CreateLinkEvent;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final MemberRepository memberRepository;
    private final LinkRepository linkRepository;
    private final LinkBundleRepository linkBundleRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public CreateLinkResponse createLink(CreateLinkCommand command) {
        LinkBundle linkBundle = getLinkBundle(command.linkBundleId());
        Link link = new Link(command.url(), command.description());
        LinkWithLinkBundle linkWithLinkBundle = new LinkWithLinkBundle(link, linkBundle);
        Long linkId = linkRepository.save(linkWithLinkBundle);
        eventPublisher.publishEvent(new CreateLinkEvent(command.url()));
        return new CreateLinkResponse(linkId);
    }

    private LinkBundle getLinkBundle(Long linkBundleId) {
        return linkBundleRepository.findById(linkBundleId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크 번들입니다.", ErrorCode.NOT_FOUND));
    }
}
