package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubTagReader;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.MemberTag;
import com.seong.shoutlink.domain.tag.Tag;
import com.seong.shoutlink.domain.tag.service.TagRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository, HubTagReader {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public List<Tag> saveHubTags(List<HubTag> tags) {
        List<TagEntity> tagEntities = tags.stream()
            .map(TagEntity::from)
            .toList();
        return tagJpaRepository.saveAll(tagEntities).stream()
            .map(TagEntity::toDomain)
            .toList();
    }

    @Override
    public void deleteHubTags(Hub hub) {
        tagJpaRepository.deleteByHubId(hub.getHubId());
    }

    @Override
    public Optional<Tag> findLatestTagByHub(Hub hub) {
        return tagJpaRepository.findLatestTagByHubId(hub.getHubId())
            .map(TagEntity::toDomain);
    }

    @Override
    public Optional<Tag> findLatestTagByMember(Member member) {
        return tagJpaRepository.findLatestTagByMemberId(member.getMemberId())
            .map(TagEntity::toDomain);
    }

    @Override
    public void deleteMemberTags(Member member) {
        tagJpaRepository.deleteByMemberId(member.getMemberId());
    }

    @Override
    public List<Tag> saveMemberTags(List<MemberTag> memberTags) {
        List<TagEntity> tagEntities = memberTags.stream()
            .map(TagEntity::from)
            .toList();
        return tagJpaRepository.saveAll(tagEntities).stream()
            .map(TagEntity::toDomain)
            .toList();
    }

    @Override
    public List<HubTagResult> findTagsInHubs(List<Hub> hubs) {
        Map<Long, Hub> hubMap = hubs.stream()
            .collect(Collectors.toMap(Hub::getHubId, hub -> hub));
        List<Long> hubIds = hubMap.keySet().stream()
            .toList();
        List<HubTagEntity> hubTagEntities = tagJpaRepository.findTagsInHubIds(hubIds);
        return hubTagEntities.stream()
            .map(hubTagEntity -> {
                Hub hub = hubMap.get(hubTagEntity.getHubId());
                return new HubTagResult(hubTagEntity.getTagId(), hubTagEntity.getName(), hub);
            })
            .toList();
    }
}
