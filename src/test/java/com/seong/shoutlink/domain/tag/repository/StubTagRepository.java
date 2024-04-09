package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.Tag;
import com.seong.shoutlink.domain.tag.service.TagRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubTagRepository implements TagRepository {

    private final Map<Long, Tag> memory = new HashMap<>();

    public void stub(Tag... tags) {
        for (Tag tag : tags) {
            memory.put(nextId(), tag);
        }
    }

    private Long nextId() {
        return (long) (memory.size() + 1);
    }

    public long count() {
        return memory.size();
    }

    @Override
    public List<Tag> saveAll(List<HubTag> hubTags) {
        for (HubTag hubTag : hubTags) {
            memory.put(nextId(), hubTag.getTag());
        }
        return memory.entrySet().stream().map(entry -> {
            Tag value = entry.getValue();
            return new Tag(entry.getKey(), value.getName(), value.getCreatedAt(), value.getUpdatedAt());
        }).toList();
    }

    @Override
    public long deleteHubTags(Hub hub) {
        int size = memory.size();
        memory.clear();
        return size;
    }
}
