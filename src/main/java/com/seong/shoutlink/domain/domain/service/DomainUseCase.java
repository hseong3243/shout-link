package com.seong.shoutlink.domain.domain.service;

import com.seong.shoutlink.domain.domain.service.request.FindDomainCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainLinksCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.domain.service.response.FindDomainDetailResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainLinksResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.UpdateDomainResponse;

public interface DomainUseCase {

    UpdateDomainResponse updateDomain(UpdateDomainCommand command);

    FindRootDomainsResponse findRootDomains(FindRootDomainsCommand command);

    FindDomainsResponse findDomains(FindDomainsCommand command);

    FindDomainDetailResponse findDomain(FindDomainCommand command);

    FindDomainLinksResponse findDomainLinks(FindDomainLinksCommand command);
}
