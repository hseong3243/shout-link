package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubTagReader;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import com.seong.shoutlink.domain.hub.service.result.TagResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubHubTagReader implements HubTagReader {

    private final Map<Long, HubTagResult> hubTagMap = new HashMap<>();

    public void stub(HubTagResult... results) {
        for (HubTagResult result : results) {
            hubTagMap.put((long) (hubTagMap.size()+1), result);
        }
    }

    @Override
    public List<HubTagResult> findTagsInHubs(List<Hub> hubs) {
        return hubTagMap.values().stream().toList();
    }

    @Override
    public List<TagResult> searchTags(String tagKeyword) {
        return hubTagMap.values().stream()
            .map(hubTag -> new TagResult(hubTag.tagId(), hubTag.name()))
            .toList();
    }
}
