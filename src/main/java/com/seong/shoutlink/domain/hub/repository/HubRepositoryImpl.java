package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.HubWithMembers;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.hubMember.repository.HubMemberEntity;
import com.seong.shoutlink.domain.hubMember.repository.HubMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository hubJpaRepository;
    private final HubMemberJpaRepository hubMemberJpaRepository;

    @Override
    public Long save(HubWithMembers hubWithMembers) {
        HubEntity hubEntity = HubEntity.create(hubWithMembers.getHub());
        hubJpaRepository.save(hubEntity);
        HubMemberEntity hubMemberEntity
            = HubMemberEntity.create(hubWithMembers.getHub(), hubWithMembers.getMember());
        hubMemberJpaRepository.save(hubMemberEntity);
        return hubEntity.getHubId();
    }
}
