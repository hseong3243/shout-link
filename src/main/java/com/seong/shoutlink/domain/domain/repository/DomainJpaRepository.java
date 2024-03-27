package com.seong.shoutlink.domain.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DomainJpaRepository extends JpaRepository<DomainEntity, Long> {

    Optional<DomainEntity> findByRootDomain(String rootDomain);

    @Query("select d.rootDomain from DomainEntity d")
    List<String> findRootDomains();
}
