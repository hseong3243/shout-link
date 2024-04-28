package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.member.Member;

public interface HubMemberRepository {

    boolean isHubMember(Hub hub, Member member);
}
