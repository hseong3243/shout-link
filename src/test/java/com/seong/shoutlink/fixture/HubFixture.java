package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.member.Member;

public final class HubFixture {

    public static final long HUB_ID = 1L;
    public static final String NAME = "테스트 허브";
    public static final String DESCRIPTION = "허브 설명";
    public static final boolean IS_PRIVATE = false;

    public static Hub hub() {
        return new Hub(HUB_ID, NAME, DESCRIPTION, IS_PRIVATE);
    }

    public static HubWithMaster hubWithMaster(Member member) {
        return new HubWithMaster(hub(), member);
    }
}
