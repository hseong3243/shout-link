package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.DomainRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubDomainRepository implements DomainRepository {

    private final Map<Long, Domain> memory = new HashMap<>();

    public void stub(Domain domain) {
        memory.put(nextId(), domain);
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

    private Long nextId() {
        return (long) (memory.size() + 1);
    }
}
