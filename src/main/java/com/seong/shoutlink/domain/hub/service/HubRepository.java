package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import java.util.Optional;

public interface HubRepository {

    Long save(Hub hub);

    Optional<Hub> findById(Long hubId);
}
