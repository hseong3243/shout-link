package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMembers;
import com.seong.shoutlink.domain.hub.service.event.CreateHubEvent;
import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubService {

    private final MemberRepository memberRepository;
    private final HubRepository hubRepository;
    private final EventPublisher eventPublisher;

    public CreateHubResponse createHub(CreateHubCommand command) {
        Member member = getMember(command.memberId());
        Hub hub = new Hub(command.name(), command.description());
        Long hubId = hubRepository.save(new HubWithMembers(hub, member));
        eventPublisher.publishEvent(new CreateHubEvent(hubId));
        return new CreateHubResponse(hubId);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND));
    }
}
