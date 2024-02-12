package com.seong.shoutlink.domain.hub;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.member.Member;
import lombok.Getter;

@Getter
public class HubWithMaster {

    private final Hub hub;
    private final Member member;

    public HubWithMaster(Hub hub, Member member) {
        this.hub = hub;
        this.member = member;
    }

    public void checkMasterAuthority(Long memberId) {
        if(member.isEqualToMemberId(memberId)) {
            return;
        }
        throw new ShoutLinkException("권한이 없습니다.", ErrorCode.UNAUTHORIZED);
    }
}
