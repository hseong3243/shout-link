package com.seong.shoutlink.domain.link.link.repository;

import com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {

    Page<LinkEntity> findAllByLinkBundleId(Long linkBundleId, Pageable pageable);

    @Query("select new com.seong.shoutlink.domain.link.linkdomain.service.result.LinkDomainLinkResult(l.linkId, l.url, count(l.url))"
        + " from LinkEntity l"
        + " where l.linkDomain.linkDomainId = :linkDomainId"
        + " group by l.url"
        + " order by count(l.url) desc")
    Page<LinkDomainLinkResult> findDomainLinks(@Param("linkDomainId") Long linkDomainId, Pageable pageable);

    List<LinkEntity> findAllByLinkBundleLinkBundleIdIn(List<Long> linkBundleIds);

    @Query("select l from LinkEntity l "
        + "join fetch MemberLinkBundleEntity lb "
        + "where l.linkId = :linkId and lb.member.memberId = :memberId")
    Optional<LinkEntity> findByIdAndMemberId(@Param("linkId") Long linkId, @Param("memberId") Long memberId);

    @Query("select l from LinkEntity l "
        + "join fetch HubLinkBundleEntity lb "
        + "where l.linkId = :linkId and lb.hub.hubId = :hubId")
    Optional<LinkEntity> findByIdAndHubId(@Param("linkId") Long linkId, @Param("hubId") Long hubId);
}
