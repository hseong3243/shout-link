package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMembers;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import java.util.HashMap;
import java.util.Map;

public class StubHubRepository implements HubRepository {

    private final Map<Long, Hub> memory = new HashMap<>();

    @Override
    public Long save(HubWithMembers hubWithMembers) {
        return 1L;
    }
}
