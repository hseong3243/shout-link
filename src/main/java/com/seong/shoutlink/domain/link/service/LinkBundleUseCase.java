package com.seong.shoutlink.domain.link.service;

import com.seong.shoutlink.domain.link.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.link.service.request.FindHubLinkBundlesCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinkBundlesResponse;

public interface LinkBundleUseCase {

    CreateLinkBundleResponse createLinkBundle(CreateLinkBundleCommand command);

    FindLinkBundlesResponse findLinkBundles(FindLinkBundlesCommand command);

    CreateLinkBundleResponse createHubLinkBundle(CreateHubLinkBundleCommand command);

    FindLinkBundlesResponse findHubLinkBundles(FindHubLinkBundlesCommand command);
}
