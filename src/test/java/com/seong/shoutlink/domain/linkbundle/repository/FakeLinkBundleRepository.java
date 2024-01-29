package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import java.util.HashMap;
import java.util.Map;

public class FakeLinkBundleRepository implements LinkBundleRepository {

    private final Map<Long, LinkBundle> memory = new HashMap<>();

    @Override
    public Long save(LinkBundle linkBundle) {
        long nextId = getNextId();
        memory.put(nextId, linkBundle);
        return nextId;
    }

    private long getNextId() {
        return memory.keySet().size() + 1;
    }
}
