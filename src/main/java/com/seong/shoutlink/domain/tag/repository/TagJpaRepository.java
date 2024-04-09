package com.seong.shoutlink.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {

    @Query("delete from HubTagEntity t where t.hubId=:hubId")
    long deleteByHubId(@Param("hubId") Long hubId);
}
