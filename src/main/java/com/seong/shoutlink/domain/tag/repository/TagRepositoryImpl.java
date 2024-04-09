package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.Tag;
import com.seong.shoutlink.domain.tag.service.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public List<Tag> saveAll(List<HubTag> tags) {
        List<TagEntity> tagEntities = tags.stream()
            .map(TagEntity::from)
            .toList();
        return tagJpaRepository.saveAll(tagEntities).stream()
            .map(TagEntity::toDomain)
            .toList();
    }

    @Override
    public long deleteHubTags(Hub hub) {
        return tagJpaRepository.deleteByHubId(hub.getHubId());
    }
}