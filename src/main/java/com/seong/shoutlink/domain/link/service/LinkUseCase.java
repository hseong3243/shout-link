package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.link.service.request.CreateHubLinkCommand;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.request.FindHubLinksCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.service.response.CreateHubLinkResponse;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinksResponse;

public interface LinkUseCase {

    CreateLinkResponse createLink(CreateLinkCommand command);

    FindLinksResponse findLinks(FindLinksCommand command);

    CreateHubLinkResponse createHubLink(CreateHubLinkCommand command);

    FindLinksResponse findHubLinks(FindHubLinksCommand command);
}