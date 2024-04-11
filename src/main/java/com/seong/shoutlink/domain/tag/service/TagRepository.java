package com.seong.shoutlink.domain.tag.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.MemberTag;
import com.seong.shoutlink.domain.tag.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {

    List<Tag> saveAll(List<HubTag> tags);

    void deleteHubTags(Hub hub);

    Optional<Tag> findLatestTagByHub(Hub hub);

    Optional<Tag> findLatestTagByMember(Member member);

    void deleteMemberTags(Member member);

    List<Tag> saveMemberTags(List<MemberTag> memberTags);
}
