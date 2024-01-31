package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.link.LinkWithLinkBundle;

public interface LinkRepository {

    Long save(LinkWithLinkBundle linkWithLinkBundle);
}
