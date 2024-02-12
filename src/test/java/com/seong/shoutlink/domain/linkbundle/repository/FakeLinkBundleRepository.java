package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeLinkBundleRepository implements LinkBundleRepository {

    private final Map<Long, LinkBundle> memory = new HashMap<>();

    public FakeLinkBundleRepository(LinkBundle... linkBundles) {
        for (LinkBundle linkBundle : linkBundles) {
            memory.put(getNextId(), linkBundle);
        }
    }

    public void stub(LinkBundle... linkBundles) {
        memory.clear();
        for (LinkBundle linkBundle : linkBundles) {
            memory.put(getNextId(), linkBundle);
        }
    }

    @Override
    public Long save(MemberLinkBundle memberLinkBundle) {
        return 1L;
    }

    @Override
    public void updateDefaultBundleFalse(Member member) {
        List<LinkBundle> defaultLinkBundle = memory.values().stream()
            .filter(LinkBundle::isDefault)
            .toList();
        defaultLinkBundle.forEach(lb -> {
            memory.remove(lb.getLinkBundleId());
            memory.put(lb.getLinkBundleId(),
                new LinkBundle(lb.getDescription(), false));
        });
    }

    @Override
    public Optional<LinkBundle> findById(Long linkBundleId) {
        return memory.values().stream()
            .filter(lb -> lb.getLinkBundleId().equals(linkBundleId))
            .findFirst();
    }

    @Override
    public List<LinkBundle> findLinkBundlesThatMembersHave(Member member) {
        return memory.values().stream().toList();
    }

    private long getNextId() {
        return memory.keySet().size() + 1;
    }
}
