package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.tag.Tag;

public final class TagFixture {

    public static Tag tag() {
        return new Tag(1L, "태그1");
    }
}
