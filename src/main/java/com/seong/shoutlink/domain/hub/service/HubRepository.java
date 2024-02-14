package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.result.HubPaginationResult;
import java.util.Optional;

public interface HubRepository {

    Long save(Hub hub);

    Optional<Hub> findById(Long hubId);

    HubPaginationResult findHubs(int page, int size);
}
