package com.seong.shoutlink.domain.linkdomain.repository;

import com.seong.shoutlink.domain.common.Trie;
import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.service.LinkDomainRepository;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkPaginationResult;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkResult;
import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainPaginationResult;
import com.seong.shoutlink.domain.link.link.Link;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StubLinkDomainRepository implements LinkDomainRepository {

    private final Map<Long, LinkDomain> memory = new HashMap<>();
    private final Map<LinkDomain, List<Link>> domainLinks = new HashMap<>();
    private final Trie searchAutoComplete = new Trie();

    public void stub(LinkDomain linkDomain) {
        memory.put(nextId(), linkDomain);
        searchAutoComplete.insert(linkDomain.getRootDomain());
    }

    public void stub(LinkDomain linkDomain, Link... links) {
        memory.put(nextId(), linkDomain);
        domainLinks.put(linkDomain, List.of(links));
    }

    @Override
    public Optional<LinkDomain> findByRootDomain(String rootDomain) {
        return memory.values().stream()
            .filter(domain -> domain.getRootDomain().equals(rootDomain))
            .findFirst();
    }

    @Override
    public LinkDomain save(LinkDomain linkDomain) {
        Long domainId = nextId();
        memory.put(domainId, linkDomain);
        return new LinkDomain(domainId, linkDomain.getRootDomain());
    }

    @Override
    public LinkDomainPaginationResult findDomains(String keyword, int page, int size) {
        List<LinkDomain> list = memory.values().stream().toList();
        int totalElements = list.size();
        boolean hasNext = false;
        if(list.size() > size) {
            list = list.subList(0, size);
            hasNext = true;
        }
        return new LinkDomainPaginationResult(list, totalElements, hasNext);
    }

    @Override
    public Optional<LinkDomain> findById(Long domainId) {
        return memory.values().stream().findFirst();
    }

    private Long nextId() {
        return (long) (memory.size() + 1);
    }

    @Override
    public LinkDomainLinkPaginationResult findDomainLinks(LinkDomain linkDomain, int page, int size) {
        List<Link> links = domainLinks.get(linkDomain);
        List<LinkDomainLinkResult> content = links.stream()
            .map(link -> new LinkDomainLinkResult(link.getLinkId(), link.getUrl(), links.size()))
            .toList();
        return new LinkDomainLinkPaginationResult(content, links.size(), links.size() > size);
    }

    @Override
    public List<String> findRootDomains(String keyword, int size) {
        return searchAutoComplete.search(keyword, size);
    }
}
