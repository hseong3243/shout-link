package com.seong.shoutlink.domain.linkbundle.service;

import com.seong.shoutlink.domain.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface LinkBundleRepository {

    Long save(MemberLinkBundle memberLinkBundle);

    void updateDefaultBundleFalse(Member member);

    Optional<LinkBundle> findById(Long linkBundleId);

    List<LinkBundle> findLinkBundlesThatMembersHave(Member member);

    Long save(HubLinkBundle hubLinkBundle);
}
