package com.seong.shoutlink.domain.link.linkdomain.service;

import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainLinksCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.UpdateLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainDetailResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainLinksResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.UpdateLinkDomainResponse;

public interface LinkDomainUseCase {

    UpdateLinkDomainResponse updateLinkDomain(UpdateLinkDomainCommand command);

    FindRootDomainsResponse findRootDomains(FindRootDomainsCommand command);

    FindLinkDomainsResponse findLinkDomains(FindLinkDomainsCommand command);

    FindLinkDomainDetailResponse findLinkDomain(FindLinkDomainCommand command);

    FindLinkDomainLinksResponse findLinkDomainLinks(FindLinkDomainLinksCommand command);
}
