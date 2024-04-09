package com.seong.shoutlink.domain.tag.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {

    @Query("delete from HubTagEntity t where t.hubId=:hubId")
    long deleteByHubId(@Param("hubId") Long hubId);

    @Query("select t from HubTagEntity t"
        + " where t.hubId=:hubId"
        + " order by t.createdAt"
        + " limit 1")
    Optional<TagEntity> findLatestTagByHubId(Long hubId);
}
