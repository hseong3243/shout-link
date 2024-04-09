package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.link.Link;
import java.util.ArrayList;
import java.util.List;

public final class LinkFixture {

    public static final long LINK_ID = 1L;
    public static final String URL = "https://github.com/hseong3243";
    public static final String DESCRIPTION = "관심있는 곳";

    public static Link link() {
        return new Link(LINK_ID, URL, DESCRIPTION);
    }

    public static List<Link> links(int count) {
        List<Link> links = new ArrayList<>();
        for(int i=1; i<=count; i++) {
            links.add(new Link((long) i, URL, DESCRIPTION));
        }
        return links;
    }
}
