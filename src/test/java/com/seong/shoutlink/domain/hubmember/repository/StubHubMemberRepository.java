package com.seong.shoutlink.domain.hubmember.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubMemberRepository;
import com.seong.shoutlink.domain.member.Member;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubHubMemberRepository implements HubMemberRepository {

    private final Map<Long, Hub> hubs = new HashMap<>();
    private final Map<Long, List<Member>> hubMembers = new HashMap<>();

    public void stub(Hub hub, Member... members) {
        long nextKey = getNextKey();
        hubs.put(nextKey, hub);
        hubMembers.put(nextKey, Arrays.asList(members));
    }

    private long getNextKey() {
        return hubs.size() + 1;
    }

    @Override
    public boolean isHubMember(Hub hub, Member member) {
        Long hubId = hub.getHubId();
        return hubMembers.get(hubId).contains(member);
    }
}