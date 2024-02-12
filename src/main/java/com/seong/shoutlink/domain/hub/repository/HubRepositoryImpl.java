package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.hubMember.repository.HubMemberEntity;
import com.seong.shoutlink.domain.hubMember.repository.HubMemberJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository hubJpaRepository;
    private final HubMemberJpaRepository hubMemberJpaRepository;

    @Override
    @Transactional
    public Long save(HubWithMaster hubWithMaster) {
        HubEntity hubEntity = HubEntity.create(hubWithMaster.getHub());
        hubJpaRepository.save(hubEntity);
        HubMemberEntity hubMemberEntity
            = HubMemberEntity.create(hubWithMaster.getHub(), hubWithMaster.getMember());
        hubMemberJpaRepository.save(hubMemberEntity);
        return hubEntity.getHubId();
    }

    @Override
    public Optional<HubWithMaster> findByIdWithHubMaster(Long hubId) {
        return Optional.empty();
    }
}
