package com.seong.shoutlink.domain.tag.service;

import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.Tag;
import java.util.List;

public interface TagRepository {

    List<Tag> saveAll(List<HubTag> tags);
}
