package com.seong.shoutlink.domain.tag.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {

    List<Tag> saveAll(List<HubTag> tags);

    long deleteHubTags(Hub hub);

    Optional<Tag> findLatestTagByHub(Hub hub);
}
