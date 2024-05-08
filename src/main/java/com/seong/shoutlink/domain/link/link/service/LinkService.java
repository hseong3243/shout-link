package com.seong.shoutlink.domain.link.link.service;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubMemberRepository;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLink;
import com.seong.shoutlink.domain.link.link.service.event.CreateHubLinkEvent;
import com.seong.shoutlink.domain.link.link.service.event.CreateMemberLinkEvent;
import com.seong.shoutlink.domain.link.link.service.request.CreateHubLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.DeleteHubLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.DeleteLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.FindHubLinksCommand;
import com.seong.shoutlink.domain.link.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.link.service.response.CreateHubLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.DeleteLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.FindLinksResponse;
import com.seong.shoutlink.domain.link.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import jakarta.annotation.Nullable;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService implements LinkUseCase {

    private final MemberRepository memberRepository;
    private final HubRepository hubRepository;
    private final HubMemberRepository hubMemberRepository;
    private final LinkRepository linkRepository;
    private final LinkBundleRepository linkBundleRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public CreateLinkResponse createLink(CreateLinkCommand command) {
        Member member = getMember(command.memberId());
        LinkBundle linkBundle = getLinkBundle(command.linkBundleId(), member);
        Link link = new Link(command.url(), command.description());
        LinkBundleAndLink linkBundleAndLink = new LinkBundleAndLink(link, linkBundle);
        Long linkId = linkRepository.save(linkBundleAndLink);
        eventPublisher.publishEvent(
            new CreateMemberLinkEvent(linkId, link.getUrl(), member.getMemberId()));
        return new CreateLinkResponse(linkId);
    }

    @Transactional(readOnly = true)
    @Override
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

    @Override
    @Transactional
    public CreateHubLinkResponse createHubLink(CreateHubLinkCommand command) {
        Hub hub = getHub(command.hubId());
        Member member = getMember(command.memberId());
        hub.checkMasterAuthority(member);

        LinkBundle hubLinkBundle = getHubLinkBundle(command.linkBundleId(), hub);
        Link link = new Link(command.url(), command.description());
        Long linkId = linkRepository.save(new LinkBundleAndLink(link, hubLinkBundle));
        eventPublisher.publishEvent(new CreateHubLinkEvent(linkId, link.getUrl(), hub.getHubId()));
        return new CreateHubLinkResponse(linkId);
    }

    @Override
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

    @Override
    @Transactional
    public DeleteLinkResponse deleteLink(DeleteLinkCommand command) {
        Member member = getMember(command.memberId());
        Link link = linkRepository.findMemberLink(command.linkId(), member)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크입니다.", ErrorCode.NOT_FOUND));
        linkRepository.delete(link);
        return new DeleteLinkResponse(link.getLinkId());
    }

    @Override
    @Transactional
    public DeleteLinkResponse deleteHubLink(DeleteHubLinkCommand command) {
        Hub hub = getHub(command.hubId());
        Member member = getMember(command.memberId());
        hub.checkMasterAuthority(member);
        Link link = linkRepository.findHubLink(command.linkId(), hub)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 링크입니다.", ErrorCode.NOT_FOUND));
        linkRepository.delete(link);
        return new DeleteLinkResponse(link.getLinkId());
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
