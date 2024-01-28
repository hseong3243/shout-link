package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import java.util.HashMap;
import java.util.Map;

public class FakeLinkRepository implements LinkRepository {

    private final Map<Long, Link> memory = new HashMap<>();

    @Override
    public Long save(Link link) {
        long nextId = memory.keySet().size() + 1;
        memory.put(nextId, link);
        return nextId;
    }
}