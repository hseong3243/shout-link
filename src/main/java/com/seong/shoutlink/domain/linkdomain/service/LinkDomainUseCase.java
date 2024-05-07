package com.seong.shoutlink.domain.linkdomain.service;

import com.seong.shoutlink.domain.linkdomain.service.request.FindLinkDomainCommand;
import com.seong.shoutlink.domain.linkdomain.service.request.FindLinkDomainLinksCommand;
import com.seong.shoutlink.domain.linkdomain.service.request.FindLinkDomainsCommand;
import com.seong.shoutlink.domain.linkdomain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.linkdomain.service.request.UpdateLinkDomainCommand;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainDetailResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainLinksResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainsResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.UpdateLinkDomainResponse;

public interface LinkDomainUseCase {

    UpdateLinkDomainResponse updateLinkDomain(UpdateLinkDomainCommand command);

    FindRootDomainsResponse findRootDomains(FindRootDomainsCommand command);

    FindLinkDomainsResponse findLinkDomains(FindLinkDomainsCommand command);

    FindLinkDomainDetailResponse findLinkDomain(FindLinkDomainCommand command);

    FindLinkDomainLinksResponse findLinkDomainLinks(FindLinkDomainLinksCommand command);
}
