package com.seong.shoutlink.domain.hub;

import com.seong.shoutlink.domain.member.Member;
import lombok.Getter;

@Getter
public class HubWithMembers {

    private final Hub hub;
    private final Member member;

    public HubWithMembers(Hub hub, Member member) {
        this.hub = hub;
        this.member = member;
    }
}
