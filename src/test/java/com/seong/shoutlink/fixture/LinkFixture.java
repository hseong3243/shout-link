package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.link.Link;

public final class LinkFixture {

    public static final long LINK_ID = 1L;
    public static final String URL = "url";
    public static final String DESCRIPTION = "관심있는 곳";

    public static Link link() {
        return new Link(LINK_ID, URL, DESCRIPTION);
    }
}
