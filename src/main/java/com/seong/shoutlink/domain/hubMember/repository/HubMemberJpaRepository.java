package com.seong.shoutlink.domain.hubMember.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HubMemberJpaRepository extends JpaRepository<HubMemberEntity, Long> {

    @Query("select hm from HubMemberEntity hm "
        + "join hm.hubEntity h "
        + "where h.hubId = :hubId "
        + "and hm.hubMemberRole = com.seong.shoutlink.domain.hubMember.HubMemberRole.MASTER")
    Optional<HubMemberEntity> findHubMasterByHubIdWithHub(@Param("hubId") Long hubId);

    @Query("select hm from HubMemberEntity hm "
        + "join fetch hm.hubEntity h "
        + "where hm.hubMemberRole = com.seong.shoutlink.domain.hubMember.HubMemberRole.MASTER")
    Page<HubMemberEntity> findHubs(PageRequest pageRequest);
}
