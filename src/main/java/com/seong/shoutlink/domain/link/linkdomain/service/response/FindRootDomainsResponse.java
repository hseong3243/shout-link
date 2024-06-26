package com.seong.shoutlink.domain.link.linkdomain.service.response;

import java.util.List;

public record FindRootDomainsResponse(List<String> rootDomains) {

    public static FindRootDomainsResponse from(List<String> rootDomains) {
        return new FindRootDomainsResponse(rootDomains);
    }
}
