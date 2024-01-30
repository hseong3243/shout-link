package com.seong.shoutlink.domain.linkbundle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkBundleJpaRepository extends JpaRepository<LinkBundleEntity, Long> {

    @Modifying
    @Query("update LinkBundleEntity lb set lb.isDefault = false"
        + " where lb.isDefault = true and lb.memberId = :memberId")
    void updateDefaultBundleFalse(@Param("memberId") Long memberId);
}
