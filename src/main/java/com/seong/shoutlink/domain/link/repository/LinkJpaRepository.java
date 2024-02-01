package com.seong.shoutlink.domain.link.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {

    Page<LinkEntity> findAllByLinkBundleId(Long linkBundleId, Pageable pageable);
}
