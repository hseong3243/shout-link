package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.DomainRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DomainRepositoryImpl implements DomainRepository {

    private final DomainJpaRepository domainJpaRepository;
    private final DomainCacheRepository domainCacheRepository;

    @Override
    public Optional<Domain> findByRootDomain(String rootDomain) {
        return domainJpaRepository.findByRootDomain(rootDomain)
            .map(DomainEntity::toDomain);
    }

    @Override
    public Domain save(Domain domain) {
        return domainJpaRepository.save(DomainEntity.create(domain))
            .toDomain();
    }

    @Override
    public List<String> findRootDomains(String keyword, int size) {
        return domainCacheRepository.findRootDomains(keyword, size);
    }

    @Override
    public void synchronizeRootDomains() {
        domainJpaRepository.findRootDomains().forEach(domainCacheRepository::insert);

    }
}
