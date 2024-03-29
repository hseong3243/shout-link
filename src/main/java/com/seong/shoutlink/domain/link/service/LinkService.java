package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.hubmember.service.HubMemberRepository;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.event.CreateLinkEvent;
import com.seong.shoutlink.domain.link.service.request.CreateHubLinkCommand;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.request.FindHubLinksCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.service.response.CreateHubLinkResponse;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinksResponse;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import jakarta.annotation.Nullable;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final MemberRepository memberRepository;
    private final HubRepository hubRepository;
    private final HubMemberRepository hubMemberRepository;
    private final LinkRepository linkRepository;
    private final LinkBundleRepository linkBundleRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public CreateLinkResponse createLink(CreateLinkCommand command) {
        Member member = getMember(command.memberId());
        LinkBundle linkBundle = getLinkBundle(command.linkBundleId(), member);
        Link link = new Link(command.url(), command.description());
        LinkWithLinkBundle linkWithLinkBundle = new LinkWithLinkBundle(link, linkBundle);
        Long linkId = linkRepository.save(linkWithLinkBundle);
        eventPublisher.publishEvent(new CreateLinkEvent(linkId, command.url()));
        return new CreateLinkResponse(linkId);
    }

    @Transactional(readOnly = true)
    public FindLinksResponse findLinks(FindLinksCommand command) {
        Member member = getMember(command.memberId());
        LinkBundle linkBundle = getLinkBundle(command.linkBundleId(), member);
        LinkPaginationResult result = linkRepository.findLinks(
            linkBundle,
            command.page(),
            command.size());
        return FindLinksResponse.of(result.links(), result.totalElements(), result.hasNext());
    }

    private LinkBundle getLinkBundle(Long linkBundleId, Member member) {
        return linkBundleRepository.findMemberLinkBundle(linkBundleId, member)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크 묶음입니다.", ErrorCode.NOT_FOUND));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND));
    }

    @Transactional
    public CreateHubLinkResponse createHubLink(CreateHubLinkCommand command) {
        Hub hub = getHub(command.hubId());
        hub.checkMasterAuthority(command.memberId());

        LinkBundle hubLinkBundle = getHubLinkBundle(command.linkBundleId(), hub);
        Link link = new Link(command.url(), command.description());
        Long linkId = linkRepository.save(new LinkWithLinkBundle(link, hubLinkBundle));
        eventPublisher.publishEvent(new CreateLinkEvent(linkId, command.url()));
        return new CreateHubLinkResponse(linkId);
    }

    @Transactional(readOnly = true)
    public FindLinksResponse findHubLinks(FindHubLinksCommand command) {
        Hub hub = getHub(command.hubId());
        if(hub.isPrivate()) {
            checkAuthenticated(command.nullableMemberId());
            Member member = getMember(command.nullableMemberId());
            checkHubMemberAuthority(hub, member);
        }

        LinkBundle hubLinkBundle = getHubLinkBundle(command.linkBundleId(), hub);
        LinkPaginationResult result = linkRepository.findLinks(hubLinkBundle, command.page(),
            command.size());
        return FindLinksResponse.of(result.links(), result.totalElements(), result.hasNext());
    }

    private void checkAuthenticated(@Nullable Long nullableMemberId) {
        if(Objects.isNull(nullableMemberId)) {
            throw new ShoutLinkException("인증되지 않은 사용자입니다.", ErrorCode.UNAUTHENTICATED);
        }
    }

    private void checkHubMemberAuthority(Hub hub, Member member) {
        if(hubMemberRepository.isHubMember(hub, member)) {
            return;
        }
        throw new ShoutLinkException("권한이 없습니다.", ErrorCode.UNAUTHORIZED);
    }

    private LinkBundle getHubLinkBundle(Long linkBundleId, Hub hub) {
        return linkBundleRepository.findHubLinkBundle(linkBundleId, hub)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크 묶음입니다.", ErrorCode.NOT_FOUND));
    }

    private Hub getHub(Long hubId) {
        return hubRepository.findById(hubId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 허브입니다.", ErrorCode.NOT_FOUND));

    }
}
