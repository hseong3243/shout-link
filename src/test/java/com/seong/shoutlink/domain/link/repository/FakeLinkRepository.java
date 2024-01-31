package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import java.util.HashMap;
import java.util.Map;

public class FakeLinkRepository implements LinkRepository {

    private final Map<Long, Link> memory = new HashMap<>();

    @Override
    public Long save(LinkWithLinkBundle linkWithLinkBundle) {
        long nextId = getNextId();
        memory.put(nextId, linkWithLinkBundle.getLink());
        return nextId;
    }

    private long getNextId() {
        long nextId = memory.keySet().size() + 1;
        return nextId;
    }
}
