package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.DomainRepository;
import java.util.List;
import com.seong.shoutlink.domain.domain.service.result.DomainPaginationResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public DomainPaginationResult findDomains(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<DomainEntity> domains = domainJpaRepository.findDomains(keyword, pageRequest);
        return new DomainPaginationResult(
            domains.map(DomainEntity::toDomain).toList(),
            domains.getTotalElements(),
            domains.hasNext());
    }

    @Override
    public Optional<Domain> findById(Long domainId) {
        return Optional.empty();
    }
}
