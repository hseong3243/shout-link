package com.seong.shoutlink.domain.link.linkbundle.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.link.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface LinkBundleRepository {

    Long save(MemberLinkBundle memberLinkBundle);

    void updateDefaultBundleFalse(Member member);

    Optional<LinkBundle> findById(Long linkBundleId);

    List<LinkBundle> findLinkBundlesThatMembersHave(Member member);

    Long save(HubLinkBundle hubLinkBundle);

    Optional<LinkBundle> findHubLinkBundle(Long linkBundleId, Hub hub);

    List<LinkBundle> findHubLinkBundles(Hub hubId);

    Optional<LinkBundle> findMemberLinkBundle(Long linkBundleId, Member member);
}
