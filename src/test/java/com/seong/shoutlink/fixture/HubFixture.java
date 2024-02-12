package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.member.Member;

public final class HubFixture {

    private static final long HUB_ID = 1L;
    private static final String NAME = "테스트 허브";
    private static final String DESCRIPTION = "허브 설명";
    private static final boolean IS_PRIVATE = false;

    public static Hub hub(Member member) {
        return new Hub(HUB_ID, member.getMemberId(), NAME, DESCRIPTION, IS_PRIVATE);
    }

    public static HubWithMaster hubWithMaster(Member member) {
        return new HubWithMaster(hub(member), member);
    }
}
