package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.HubWithMaster;
import java.util.Optional;

public interface HubRepository {

    Long save(HubWithMaster hubWithMaster);

    Optional<HubWithMaster> findByIdWithHubMaster(Long hubId);
}
