package com.seong.shoutlink.domain.linkbundle.service;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.hubmember.service.HubMemberRepository;
import com.seong.shoutlink.domain.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.request.FindHubLinkBundlesCommand;
import com.seong.shoutlink.domain.linkbundle.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundlesResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkBundleService {

    private final MemberRepository memberRepository;
    private final HubRepository hubRepository;
    private final HubMemberRepository hubMemberRepository;
    private final LinkBundleRepository linkBundleRepository;

    @Transactional
    public CreateLinkBundleResponse createLinkBundle(CreateLinkBundleCommand command) {
        Member member = getMember(command.memberId());
        if(command.isDefault()) {
            linkBundleRepository.updateDefaultBundleFalse(member);
        }
        LinkBundle linkBundle = new LinkBundle(
            command.description(),
            command.isDefault());
        MemberLinkBundle memberLinkBundle = new MemberLinkBundle(member, linkBundle);
        return new CreateLinkBundleResponse(linkBundleRepository.save(memberLinkBundle));
    }

    public FindLinkBundlesResponse findLinkBundles(FindLinkBundlesCommand command) {
        Member member = getMember(command.memberId());
        List<LinkBundle> linkBundles
            = linkBundleRepository.findLinkBundlesThatMembersHave(member);
        return FindLinkBundlesResponse.from(linkBundles);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND));
    }

    @Transactional
    public CreateLinkBundleResponse createHubLinkBundle(CreateHubLinkBundleCommand command) {
        Hub hub = getHub(command.hubId());
        hub.checkMasterAuthority(command.memberId());
        LinkBundle linkBundle = new LinkBundle(
            command.description(),
            command.isDefault());
        HubLinkBundle hubLinkBundle = new HubLinkBundle(hub, linkBundle);
        return new CreateLinkBundleResponse(linkBundleRepository.save(hubLinkBundle));
    }

    @Transactional(readOnly = true)
    public FindLinkBundlesResponse findHubLinkBundles(FindHubLinkBundlesCommand command) {
        Hub hub = getHub(command.hubId());
        if(hub.isPrivate()) {
            checkAuthenticated(command.nullableMemberId());
            Long memberId = command.nullableMemberId();
            Member member = getMember(memberId);
            checkHubMemberAuthority(hub, member);
        }
        List<LinkBundle> hubLinkBundles = linkBundleRepository.findHubLinkBundles(hub);
        return FindLinkBundlesResponse.from(hubLinkBundles);
    }

    private void checkAuthenticated(@Nullable Long memberId) {
        if(Objects.isNull(memberId)) {
            throw new ShoutLinkException("인증되지 않은 회원입니다.", ErrorCode.UNAUTHENTICATED);
        }
    }

    private void checkHubMemberAuthority(Hub hub, Member member) {
        if(hubMemberRepository.isHubMember(hub, member)) {
            return;
        }
        throw new ShoutLinkException("권한이 없습니다.", ErrorCode.UNAUTHORIZED);
    }

    private Hub getHub(Long hubId) {
        return hubRepository.findById(hubId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 허브입니다.", ErrorCode.NOT_FOUND));
    }
}
