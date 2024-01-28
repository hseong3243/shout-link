package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.service.event.CreateLinkEvent;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final MemberRepository memberRepository;
    private final LinkRepository linkRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public CreateLinkResponse createLink(CreateLinkCommand command) {
//        Member member = memberRepository.findById(command.memberId())
//            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND));
        Link link = new Link(command.url(), command.description());
        Long linkId = linkRepository.save(link);
        eventPublisher.publishEvent(new CreateLinkEvent(command.url()));
        return new CreateLinkResponse(linkId);
    }
}
