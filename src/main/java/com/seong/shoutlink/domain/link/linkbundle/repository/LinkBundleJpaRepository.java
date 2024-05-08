package com.seong.shoutlink.domain.link.linkbundle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkBundleJpaRepository extends JpaRepository<LinkBundleEntity, Long> {

    @Modifying
    @Query("update MemberLinkBundleEntity lb set lb.isDefault = false"
        + " where lb.isDefault = true and lb.member.memberId = :memberId")
    void updateDefaultBundleFalse(@Param("memberId") Long memberId);

    @Query("select lb from MemberLinkBundleEntity lb "
        + "where lb.member.memberId = :memberId")
    List<LinkBundleEntity> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select lb from HubLinkBundleEntity lb "
        + "where lb.linkBundleId = :linkBundleId and lb.hub.hubId = :hubId")
    Optional<LinkBundleEntity> findHubLinkBundle(
        @Param("linkBundleId") Long linkBundleId,
        @Param("hubId") Long hubId);

    @Query("select lb from HubLinkBundleEntity lb "
        + "where lb.hub.hubId = :hubId")
    List<LinkBundleEntity> findAllByHubId(@Param("hubId") Long hubId);

    @Query("select lb from MemberLinkBundleEntity lb "
        + "where lb.linkBundleId = :linkBundleId and lb.member.memberId = :memberId")
    Optional<LinkBundleEntity> findMemberLinkBundle(
        @Param("linkBundleId") Long linkBundleId,
        @Param("memberId") Long memberId);
}
