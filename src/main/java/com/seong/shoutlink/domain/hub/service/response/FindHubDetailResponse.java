package com.seong.shoutlink.domain.hub.service.response;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.HubWithMaster;
import com.seong.shoutlink.domain.member.Member;

public record FindHubDetailResponse(
    Long hubId,
    Long masterId,
    String name,
    String description,
    boolean isPrivate,
    String masterNickname) {

    public static FindHubDetailResponse from(HubWithMaster hubWithMaster) {
        Hub hub = hubWithMaster.getHub();
        Member member = hubWithMaster.getMember();
        return new FindHubDetailResponse(
            hub.getHubId(),
            hub.getMasterId(),
            hub.getName(),
            hub.getDescription(),
            hub.isPrivate(),
            member.getNickname());
    }
}
