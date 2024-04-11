package com.seong.shoutlink.domain.tag.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {

    @Modifying
    @Query("delete from HubTagEntity t where t.hubId=:hubId")
    void deleteByHubId(@Param("hubId") Long hubId);

    @Query("select t from HubTagEntity t"
        + " where t.hubId=:hubId"
        + " order by t.createdAt"
        + " limit 1")
    Optional<TagEntity> findLatestTagByHubId(Long hubId);

    @Query("select t from HubTagEntity t"
        + " where t.hubId in :hubIds")
    List<HubTagEntity> findTagsInHubIds(@Param("hubIds") List<Long> hubIds);
}
