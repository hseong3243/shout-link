package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import java.util.List;

public interface HubTagReader {

    List<HubTagResult> findTagsInHubs(List<Hub> hubs);
}
