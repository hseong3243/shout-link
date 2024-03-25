package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeLinkRepository implements LinkRepository {

    private final Map<Long, Link> memory = new HashMap<>();

    public FakeLinkRepository(Link... links) {
        stub(links);
    }

    public void stub(Link... links) {
        for (Link link : links) {
            memory.put(getNextId(), link);
        }
    }

    @Override
    public Long save(LinkWithLinkBundle linkWithLinkBundle) {
        long nextId = getNextId();
        memory.put(nextId, linkWithLinkBundle.getLink());
        return nextId;
    }

    private long getNextId() {
        return memory.keySet().size() + 1;
    }

    @Override
    public LinkPaginationResult findLinks(
        LinkBundle linkBundle,
        int page,
        int size) {
        List<Link> content = memory.values().stream().toList();
        return new LinkPaginationResult(content, content.size(), false);
    }

    @Override
    public void updateLinkDomain(Link link, Domain domain) {

    }

    @Override
    public Optional<Link> findById(Long linkId) {
        return Optional.ofNullable(memory.get(linkId));
    }
}
