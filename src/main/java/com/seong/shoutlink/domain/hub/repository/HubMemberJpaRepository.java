package com.seong.shoutlink.domain.hub.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HubMemberJpaRepository extends JpaRepository<HubMemberEntity, Long> {

    @Query("select hm from HubMemberEntity hm "
        + "join fetch hm.hub h "
        + "join fetch hm.member m "
        + "where h.hubId = :hubId "
        + "and hm.hubMemberRole = com.seong.shoutlink.domain.hub.repository.HubMemberRole.MASTER")
    Optional<HubMemberEntity> findHubWithMaster(@Param("hubId") Long hubId);

    @Query("select hm from HubMemberEntity hm "
        + "join fetch hm.hub h "
        + "join fetch hm.member m "
        + "where hm.hubMemberRole = com.seong.shoutlink.domain.hub.repository.HubMemberRole.MASTER")
    Page<HubMemberEntity> findHubs(PageRequest pageRequest);

    @Query("select hm from HubMemberEntity hm "
        + "join fetch hm.hub h "
        + "join fetch hm.member m "
        + "where hm.member.memberId=:memberId "
        + "and hm.hubMemberRole = com.seong.shoutlink.domain.hub.repository.HubMemberRole.MASTER")
    Page<HubMemberEntity> findMemberHubs(@Param("memberId") Long memberId, PageRequest pageRequest);

    @Query("select hm from HubTagEntity t "
        + "join HubMemberEntity hm on hm.hub.hubId = t.hubId "
        + "join fetch hm.hub h "
        + "join fetch hm.member m "
        + "where t.tagId in :tagIds "
        + "and hm.hubMemberRole = com.seong.shoutlink.domain.hub.repository.HubMemberRole.MASTER")
    Page<HubMemberEntity> findHubsContainsTagIds(@Param("tagIds") List<Long> tagIds, PageRequest pageRequest);
}
