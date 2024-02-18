package com.seong.shoutlink.domain.hubmember.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hubmember.service.HubMemberRepository;
import com.seong.shoutlink.domain.member.Member;
import org.springframework.stereotype.Repository;

@Repository
public class HubMemberRepositoryImpl implements HubMemberRepository {

    @Override
    public boolean isHubMember(Hub hub, Member member) {
        return false;
    }
}
