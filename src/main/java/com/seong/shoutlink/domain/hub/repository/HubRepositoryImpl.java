package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.HubWithMembers;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HubRepositoryImpl implements HubRepository {

    @Override
    public Long save(HubWithMembers hub) {
        return 1L;
    }
}
