package com.seong.shoutlink.domain.linkbundle.service;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundlesResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkBundleService {

    private final MemberRepository memberRepository;
    private final HubRepository hubRepository;
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
        HubWithMaster hubWithMaster = getHubWithMaster(command.hubId());
        hubWithMaster.checkMasterAuthority(command.memberId());
        LinkBundle linkBundle = new LinkBundle(
            command.description(),
            command.isDefault());
        HubLinkBundle hubLinkBundle = new HubLinkBundle(hubWithMaster.getHub(), linkBundle);
        return new CreateLinkBundleResponse(linkBundleRepository.save(hubLinkBundle));
    }

    private HubWithMaster getHubWithMaster(Long hubId) {
        return hubRepository.findByIdWithHubMaster(hubId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 허브입니다.", ErrorCode.NOT_FOUND));
    }
}
