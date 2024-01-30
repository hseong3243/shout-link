package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeLinkBundleRepository implements LinkBundleRepository {

    private final Map<Long, LinkBundle> memory = new HashMap<>();

    @Override
    public Long save(LinkBundle linkBundle) {
        long nextId = getNextId();
        linkBundle.initId(nextId);
        memory.put(nextId, linkBundle);
        return nextId;
    }

    @Override
    public void updateDefaultBundleFalse(Member member) {
        List<LinkBundle> defaultLinkBundle = memory.values().stream()
            .filter(linkBundle -> linkBundle.isDefault() && linkBundle.getMember().equals(member))
            .toList();
        defaultLinkBundle.forEach(lb -> {
            memory.remove(lb.getLinkBundleId());
            memory.put(lb.getLinkBundleId(),
                new LinkBundle(lb.getDescription(), false, lb.getMember()));
        });
    }

    private long getNextId() {
        return memory.keySet().size() + 1;
    }
}
