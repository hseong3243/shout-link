package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.HubWithMembers;

public interface HubRepository {

    Long save(HubWithMembers hubWithMembers);
}
