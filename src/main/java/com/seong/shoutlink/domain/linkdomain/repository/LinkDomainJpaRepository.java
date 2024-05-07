package com.seong.shoutlink.domain.linkdomain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkDomainJpaRepository extends JpaRepository<LinkDomainEntity, Long> {

    Optional<LinkDomainEntity> findByRootDomain(String rootDomain);

    @Query("select d.rootDomain from LinkDomainEntity d")
    List<String> findRootDomains();

    @Query("select d from LinkDomainEntity d"
        + " where d.rootDomain like :keyword%"
        + " order by d.createdAt desc")
    Page<LinkDomainEntity> findDomains(@Param("keyword") String keyword, Pageable pageable);
}
