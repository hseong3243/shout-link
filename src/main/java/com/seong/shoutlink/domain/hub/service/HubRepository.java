package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import java.util.Optional;

public interface HubRepository {

    Long save(Hub hub);

    Optional<HubWithMaster> findByIdWithHubMaster(Long hubId);
}
