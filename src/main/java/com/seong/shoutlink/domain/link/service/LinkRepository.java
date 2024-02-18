package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;

public interface LinkRepository {

    Long save(LinkWithLinkBundle linkWithLinkBundle);

    LinkPaginationResult findLinks(LinkBundle linkBundle, int page, int size);
}
