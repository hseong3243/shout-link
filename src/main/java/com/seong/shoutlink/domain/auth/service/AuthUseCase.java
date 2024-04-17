package com.seong.shoutlink.domain.auth.service;

import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;

public interface AuthUseCase {

    CreateMemberResponse createMember(CreateMemberCommand command);

    LoginResponse login(LoginCommand command);
}
