package com.seong.shoutlink.domain.linkbundle.service;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
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
    private final LinkBundleRepository linkBundleRepository;

    @Transactional
    public CreateLinkBundleResponse createLinkBundle(CreateLinkBundleCommand command) {
        Member member = getMember(command.memberId());
        if(command.isDefault()) {
            linkBundleRepository.updateDefaultBundleFalse(member);
        }
        LinkBundle linkBundle = new LinkBundle(
            command.description(),
            command.isDefault(),
            member);
        return new CreateLinkBundleResponse(linkBundleRepository.save(linkBundle));
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
}
