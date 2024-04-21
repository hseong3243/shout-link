package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.domain.service.result.DomainLinkResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {

    Page<LinkEntity> findAllByLinkBundleId(Long linkBundleId, Pageable pageable);

    @Query("select new com.seong.shoutlink.domain.domain.service.result.DomainLinkResult(l.linkId, l.url, count(l.url))"
        + " from LinkEntity l"
        + " where l.domainId = :domainId"
        + " group by l.url"
        + " order by count(l.url) desc")
    Page<DomainLinkResult> findDomainLinks(@Param("domainId") Long domainId, Pageable pageable);

    List<LinkEntity> findAllByLinkBundleIdIn(List<Long> linkBundleIds);

    @Query("select l from LinkEntity l "
        + "join MemberLinkBundleEntity lb on l.linkBundleId = lb.linkBundleId "
        + "where l.linkId = :linkId and lb.memberId = :memberId")
    Optional<LinkEntity> findByIdAndMemberId(@Param("linkId") Long linkId, @Param("memberId") Long memberId);
}
