package com.seong.shoutlink.domain.tag.service;

import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand;
import com.seong.shoutlink.domain.tag.service.ai.GeneratedTag;
import java.util.List;

public interface AutoGenerativeClient {

    List<GeneratedTag> generateTags(GenerateAutoTagCommand command);
}
