package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import java.util.Optional;

public interface LinkRepository {

    Long save(LinkWithLinkBundle linkWithLinkBundle);

    LinkPaginationResult findLinks(LinkBundle linkBundle, int page, int size);

    void updateLinkDomain(Link link, Domain domain);

    Optional<Link> findById(Long linkId);
}
