package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.request.FindHubCommand;
import com.seong.shoutlink.domain.hub.service.request.FindMyHubsCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubDetailResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubsCommand;
import com.seong.shoutlink.domain.hub.service.response.FindHubsResponse;
import com.seong.shoutlink.domain.hub.service.request.SearchHubsCommand;

public interface HubUseCase {

    CreateHubResponse createHub(CreateHubCommand command);

    FindHubsResponse findHubs(FindHubsCommand command);

    FindHubsResponse findMemberHubs(FindMyHubsCommand command);

    FindHubDetailResponse findHub(FindHubCommand command);

    FindHubsResponse searchHubs(SearchHubsCommand command);
}
