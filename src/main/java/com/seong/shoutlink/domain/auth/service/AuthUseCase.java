package com.seong.shoutlink.domain.auth.service;

import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;

public interface AuthUseCase {

    LoginResponse login(LoginCommand command);
}
