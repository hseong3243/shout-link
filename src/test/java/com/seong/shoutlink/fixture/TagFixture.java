package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.tag.Tag;
import java.time.LocalDateTime;

public final class TagFixture {

    public static final long TAG_ID = 1L;
    public static final String TAG_NAME = "태그1";
    public static final LocalDateTime MORE_THEN_A_DAY = LocalDateTime.now().minusHours(25);

    public static Tag tag() {
        return new Tag(TAG_ID, TAG_NAME, MORE_THEN_A_DAY, MORE_THEN_A_DAY);
    }
}
