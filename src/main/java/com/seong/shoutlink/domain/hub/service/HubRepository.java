package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.hub.service.result.HubPaginationResult;
import com.seong.shoutlink.domain.hub.service.result.TagResult;
import com.seong.shoutlink.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface HubRepository {

    Long save(Hub hub);

    Optional<Hub> findById(Long hubId);

    HubPaginationResult findHubs(int page, int size);

    Optional<HubWithMaster> findHubWithMaster(Long hubId);

    HubPaginationResult findMemberHubs(Member member, int page, int size);

    HubPaginationResult findHubsByTags(List<TagResult> tagResults, int page, int size);
}
