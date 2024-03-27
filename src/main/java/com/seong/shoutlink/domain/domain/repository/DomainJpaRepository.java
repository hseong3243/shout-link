package com.seong.shoutlink.domain.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DomainJpaRepository extends JpaRepository<DomainEntity, Long> {

    Optional<DomainEntity> findByRootDomain(String rootDomain);

    @Query("select d.rootDomain from DomainEntity d")
    List<String> findRootDomains();

    @Query("select d from DomainEntity d"
        + " where d.rootDomain like :keyword%"
        + " order by d.createdAt desc")
    Page<DomainEntity> findDomains(@Param("keyword") String keyword, Pageable pageable);
}
