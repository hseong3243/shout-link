package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.member.Member;

public interface LinkRepository {

    Long save(LinkWithLinkBundle linkWithLinkBundle);

    LinkPaginationResult findLinks(Member member, LinkBundle linkBundle, int page, int size);
}
