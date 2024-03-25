package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.service.DomainRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DomainRepositoryImpl implements DomainRepository {

    private final DomainJpaRepository domainJpaRepository;

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
}
