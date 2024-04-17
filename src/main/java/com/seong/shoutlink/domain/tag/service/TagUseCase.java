package com.seong.shoutlink.domain.tag.service;

import com.seong.shoutlink.domain.tag.service.request.AutoCreateHubTagCommand;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateMemberTagCommand;
import com.seong.shoutlink.domain.tag.service.response.CreateTagResponse;

public interface TagUseCase {

    CreateTagResponse autoCreateHubTags(AutoCreateHubTagCommand command);

    CreateTagResponse autoCreateMemberTags(AutoCreateMemberTagCommand command);
}
