package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubHubRepository implements HubRepository {

    private final Map<Long, HubWithMaster> memory = new HashMap<>();

    public void stub(HubWithMaster... hubWithMasters) {
        for (HubWithMaster hubWithMaster : hubWithMasters) {
            memory.put(getNextKey(), hubWithMaster);
        }
    }

    private long getNextKey() {
        return memory.size() + 1;
    }

    @Override
    public Long save(Hub hub) {
        return 1L;
    }

    @Override
    public Optional<HubWithMaster> findByIdWithHubMaster(Long hubId) {
        return memory.values().stream().findFirst();
    }
}
