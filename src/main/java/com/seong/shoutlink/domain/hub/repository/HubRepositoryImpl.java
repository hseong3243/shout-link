package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.hub.service.result.HubPaginationResult;
import com.seong.shoutlink.domain.hub.service.result.TagResult;
import com.seong.shoutlink.domain.hubmember.repository.HubMemberEntity;
import com.seong.shoutlink.domain.hubmember.repository.HubMemberJpaRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final HubJpaRepository hubJpaRepository;
    private final HubMemberJpaRepository hubMemberJpaRepository;

    @Override
    @Transactional
    public Long save(Hub hub) {
        HubEntity hubEntity = HubEntity.create(hub);
        hubJpaRepository.save(hubEntity);
        HubMemberEntity hubMemberEntity
            = HubMemberEntity.create(hubEntity, hub.getMasterId());
        hubMemberJpaRepository.save(hubMemberEntity);
        return hubEntity.getHubId();
    }

    @Override
    public Optional<Hub> findById(Long hubId) {
        return hubMemberJpaRepository.findHubWithMaster(hubId)
            .map(HubMemberEntity::toHub);
    }

    @Override
    public HubPaginationResult findHubs(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<HubMemberEntity> hubs = hubMemberJpaRepository.findHubs(pageRequest);
        return new HubPaginationResult(
            hubs.map(HubMemberEntity::toHub).toList(),
            hubs.getTotalElements(),
            hubs.hasNext());
    }

    @Override
    public Optional<HubWithMaster> findHubWithMaster(Long hubId) {
        return findById(hubId)
            .flatMap(hub -> memberJpaRepository.findById(hub.getMasterId())
                .map(memberEntity -> new HubWithMaster(hub, memberEntity.toDomain())));
    }

    @Override
    public HubPaginationResult findMemberHubs(Member member, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<HubMemberEntity> hubs = hubMemberJpaRepository.findMemberHubs(
            member.getMemberId(), pageRequest);
        return new HubPaginationResult(
            hubs.map(HubMemberEntity::toHub).toList(),
            hubs.getTotalElements(),
            hubs.hasNext());
    }

    @Override
    public HubPaginationResult findHubsByTags(List<TagResult> tagResults, int page, int size) {
        List<Long> tagIds = tagResults.stream()
            .map(TagResult::tagId)
            .toList();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<HubMemberEntity> hubs = hubMemberJpaRepository.findHubsContainsTagIds(
            tagIds, pageRequest);
        return new HubPaginationResult(
            hubs.map(HubMemberEntity::toHub).toList(),
            hubs.getTotalElements(),
            hubs.hasNext());
    }
}
