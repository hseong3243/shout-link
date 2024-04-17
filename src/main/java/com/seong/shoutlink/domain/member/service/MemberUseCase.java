package com.seong.shoutlink.domain.member.service;

import com.seong.shoutlink.domain.member.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.member.service.response.CreateMemberResponse;

public interface MemberUseCase {

    CreateMemberResponse createMember(CreateMemberCommand command);

}
