package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.MemberTag;
import com.seong.shoutlink.domain.tag.Tag;
import com.seong.shoutlink.domain.tag.service.TagRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<Tag> saveHubTags(List<HubTag> hubTags) {
        for (HubTag hubTag : hubTags) {
            memory.put(nextId(), hubTag.getTag());
        }
        return memory.values().stream().toList();
    }

    @Override
    public void deleteHubTags(Hub hub) {
        memory.clear();
    }

    @Override
    public Optional<Tag> findLatestTagByHub(Hub hub) {
        return memory.values().stream().findFirst();
    }

    @Override
    public Optional<Tag> findLatestTagByMember(Member member) {
        return memory.values().stream().findFirst();
    }

    @Override
    public void deleteMemberTags(Member member) {
        memory.clear();
    }

    @Override
    public List<Tag> saveMemberTags(List<MemberTag> memberTags) {
        for (MemberTag memberTag : memberTags) {
            memory.put(nextId(), memberTag.getTag());
        }
        return memory.values().stream().toList();
    }
}
