package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.event.CreateHubEvent;
import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.request.FindHubCommand;
import com.seong.shoutlink.domain.hub.service.request.FindMyHubsCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubDetailResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubsCommand;
import com.seong.shoutlink.domain.hub.service.response.FindHubsResponse;
import com.seong.shoutlink.domain.hub.service.result.HubPaginationResult;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubService implements HubUseCase {

    private final MemberRepository memberRepository;
    private final HubRepository hubRepository;
    private final HubTagReader hubTagReader;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public CreateHubResponse createHub(CreateHubCommand command) {
        Member member = getMember(command.memberId());
        Hub hub = new Hub(member, command.name(), command.description(), command.isPrivate());
        Long hubId = hubRepository.save(hub);
        eventPublisher.publishEvent(new CreateHubEvent(hubId, hub.getMasterId()));
        return new CreateHubResponse(hubId);
    }

    @Override
    @Transactional(readOnly = true)
    public FindHubsResponse findHubs(FindHubsCommand command) {
        HubPaginationResult result = hubRepository.findHubs(command.page(), command.size());
        List<Hub> hubs = result.hubs();
        List<HubTagResult> tagsInHubs = hubTagReader.findTagsInHubs(hubs);
        return createFindHubResponses(result, tagsInHubs);
    }

    @Override
    @Transactional(readOnly = true)
    public FindHubsResponse findMemberHubs(FindMyHubsCommand command) {
        Member member = getMember(command.memberId());
        HubPaginationResult result = hubRepository.findMemberHubs(member, command.page(),
            command.size());
        List<Hub> hubs = result.hubs();
        List<HubTagResult> tagsInHubs = hubTagReader.findTagsInHubs(hubs);
        return createFindHubResponses(result, tagsInHubs);
    }

    private FindHubsResponse createFindHubResponses(HubPaginationResult result,
        List<HubTagResult> tagsInHubs) {
        Map<Hub, List<HubTagResult>> tagsCollectedByHub = tagsInHubs.stream()
            .collect(Collectors.groupingBy(HubTagResult::hub));
        List<FindHubResponse> findHubs = result.hubs().stream()
            .map(hub -> HubMapper.findHubResponse(hub,
                tagsCollectedByHub.getOrDefault(hub, new ArrayList<>())))
            .toList();
        return new FindHubsResponse(findHubs, result.totalElements(), result.hasNext());
    }

    @Override
    @Transactional(readOnly = true)
    public FindHubDetailResponse findHub(FindHubCommand command) {
        HubWithMaster hubWithMaster = getHubWithMaster(command.hubId());
        return FindHubDetailResponse.from(hubWithMaster);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND));
    }

    private HubWithMaster getHubWithMaster(Long hubId) {
        return hubRepository.findHubWithMaster(hubId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 허브입니다.", ErrorCode.NOT_FOUND));
    }
}
