package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.link.service.LinkOrderBy;
import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLink;
import com.seong.shoutlink.domain.link.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.member.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StubLinkRepository implements LinkRepository {

    private final Map<Long, Link> memory = new HashMap<>();

    public StubLinkRepository(Link... links) {
        stub(links);
    }

    public void stub(Link... links) {
        for (Link link : links) {
            memory.put(getNextId(), link);
        }
    }

    public int count() {
        return memory.size();
    }

    @Override
    public Long save(LinkBundleAndLink linkBundleAndLink) {
        long nextId = getNextId();
        memory.put(nextId, linkBundleAndLink.getLink());
        return nextId;
    }

    private long getNextId() {
        return memory.keySet().size() + 1;
    }

    @Override
    public LinkPaginationResult findLinks(
        LinkBundle linkBundle,
        int page,
        int size, LinkOrderBy linkOrderBy) {
        List<Link> content = memory.values().stream().toList();
        return new LinkPaginationResult(content, content.size(), false);
    }

    @Override
    public void updateLinkDomain(Link link, LinkDomain linkDomain) {

    }

    @Override
    public Optional<Link> findById(Long linkId) {
        return Optional.ofNullable(memory.get(linkId));
    }

    @Override
    public List<LinkBundleAndLink> findAllByLinkBundlesIn(List<LinkBundle> linkBundles) {
        List<LinkBundleAndLink> result = new ArrayList<>();
        List<Link> links = memory.values().stream().toList();
        for(int i=0; i<links.size(); i++) {
            LinkBundle linkBundle = linkBundles.get(i % linkBundles.size());
            result.add(new LinkBundleAndLink(links.get(i), linkBundle));
        }
        return result;
    }

    @Override
    public Optional<Link> findMemberLink(Long linkId, Member member) {
        return memory.values().stream().findFirst();
    }

    @Override
    public Optional<Link> findHubLink(Long linkId, Hub hub) {
        return memory.values().stream().findFirst();
    }

    @Override
    public void delete(Link link) {
        memory.values().remove(link);
    }
}
