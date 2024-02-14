package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubHubRepository implements HubRepository {

    private final Map<Long, Hub> memory = new HashMap<>();

    public void stub(Hub... hubs) {
        for (Hub hub : hubs) {
            memory.put(getNextKey(), hub);
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
    public Optional<Hub> findById(Long hubId) {
        return memory.values().stream().findFirst();
    }
}
