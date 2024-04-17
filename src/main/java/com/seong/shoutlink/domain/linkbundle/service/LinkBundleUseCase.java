package com.seong.shoutlink.domain.linkbundle.service;

import com.seong.shoutlink.domain.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.request.FindHubLinkBundlesCommand;
import com.seong.shoutlink.domain.linkbundle.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundlesResponse;

public interface LinkBundleUseCase {

    CreateLinkBundleResponse createLinkBundle(CreateLinkBundleCommand command);

    FindLinkBundlesResponse findLinkBundles(FindLinkBundlesCommand command);

    CreateLinkBundleResponse createHubLinkBundle(CreateHubLinkBundleCommand command);

    FindLinkBundlesResponse findHubLinkBundles(FindHubLinkBundlesCommand command);
}
