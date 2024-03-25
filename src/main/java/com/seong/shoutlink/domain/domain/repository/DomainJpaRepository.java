package com.seong.shoutlink.domain.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainJpaRepository extends JpaRepository<DomainEntity, Long> {

    Optional<DomainEntity> findByRootDomain(String rootDomain);
}
