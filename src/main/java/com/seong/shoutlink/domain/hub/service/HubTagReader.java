package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import com.seong.shoutlink.domain.hub.service.result.TagResult;
import java.util.List;

public interface HubTagReader {

    List<HubTagResult> findTagsInHubs(List<Hub> hubs);

    List<TagResult> searchTags(String tagKeyword);
}
