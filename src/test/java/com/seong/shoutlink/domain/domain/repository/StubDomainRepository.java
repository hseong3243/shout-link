package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.common.Trie;
import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.DomainRepository;
import com.seong.shoutlink.domain.domain.service.result.DomainPaginationResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StubDomainRepository implements DomainRepository {

    private final Map<Long, Domain> memory = new HashMap<>();
    private final Trie searchAutoComplete = new Trie();

    public void stub(Domain domain) {
        memory.put(nextId(), domain);
        searchAutoComplete.insert(domain.getRootDomain());
    }

    @Override
    public Optional<Domain> findByRootDomain(String rootDomain) {
        return memory.values().stream()
            .filter(domain -> domain.getRootDomain().equals(rootDomain))
            .findFirst();
    }

    @Override
    public Domain save(Domain domain) {
        Long domainId = nextId();
        memory.put(domainId, domain);
        return new Domain(domainId, domain.getRootDomain());
    }

    @Override
    public DomainPaginationResult findDomains(String keyword, int page, int size) {
        List<Domain> list = memory.values().stream().toList();
        int totalElements = list.size();
        boolean hasNext = false;
        if(list.size() > size) {
            list = list.subList(0, size);
            hasNext = true;
        }
        return new DomainPaginationResult(list, totalElements, hasNext);
    }

    @Override
    public Optional<Domain> findById(Long domainId) {
        return memory.values().stream().findFirst();
    }

    private Long nextId() {
        return (long) (memory.size() + 1);
    }

    @Override
    public List<String> findRootDomains(String keyword, int size) {
        return searchAutoComplete.search(keyword, size);
    }

    @Override
    public void synchronizeRootDomains() {
        for (Domain domain : memory.values()) {
            searchAutoComplete.insert(domain.getRootDomain());
        }
    }
}
