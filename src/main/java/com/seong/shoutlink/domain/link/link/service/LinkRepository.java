package com.seong.shoutlink.domain.link.link.service;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLink;
import com.seong.shoutlink.domain.link.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    Long save(LinkBundleAndLink linkBundleAndLink);

    LinkPaginationResult findLinks(LinkBundle linkBundle, int page, int size,
        LinkOrderBy linkOrderBy);

    void updateLinkDomain(Link link, LinkDomain linkDomain);

    Optional<Link> findById(Long linkId);

    List<LinkBundleAndLink> findAllByLinkBundlesIn(List<LinkBundle> linkBundles);

    Optional<Link> findMemberLink(Long linkId, Member member);

    Optional<Link> findHubLink(Long linkId, Hub hub);

    void delete(Link link);
}
