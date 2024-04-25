package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.hub.service.result.HubPaginationResult;
import com.seong.shoutlink.domain.hub.service.result.TagResult;
import com.seong.shoutlink.domain.member.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StubHubRepository implements HubRepository {

    private final Map<Long, Hub> memory = new HashMap<>();
    private final Map<Long, Member> masters = new HashMap<>();

    public void stub(Hub... hubs) {
        for (Hub hub : hubs) {
            memory.put(getNextKey(), hub);
        }
    }

    public void stub(Hub hub, Member master) {
        long nextKey = getNextKey();
        memory.put(nextKey, hub);
        masters.put(nextKey, master);
    }

    private long getNextKey() {
        return memory.size() + 1;
    }

    @Override
    public Long save(Hub hub) {
        return 1L;
    }

    @Override
    public Optional<Hub> findById(Long hubId) {
        return memory.values().stream().findFirst();
    }

    @Override
    public HubPaginationResult findHubs(int page, int size) {
        List<Hub> hubs = memory.values().stream().toList();
        return new HubPaginationResult(hubs, hubs.size(), hubs.size() > 10);
    }

    @Override
    public Optional<HubWithMaster> findHubWithMaster(Long hubId) {
        Optional<Hub> optionalHub = memory.values().stream().findFirst();
        Optional<Member> optionalMember = masters.values().stream().findFirst();
        return optionalHub.flatMap(
            hub -> optionalMember.map(member -> new HubWithMaster(hub, member)));
    }

    @Override
    public HubPaginationResult findMemberHubs(Member member, int page, int size) {
        return findHubs(page, size);
    }

    @Override
    public HubPaginationResult findHubsByTags(List<TagResult> tagResults, int page, int size) {
        return findHubs(page, size);
    }
}
