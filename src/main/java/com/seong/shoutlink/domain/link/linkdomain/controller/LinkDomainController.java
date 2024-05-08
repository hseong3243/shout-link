package com.seong.shoutlink.domain.link.linkdomain.controller;

import com.seong.shoutlink.domain.link.linkdomain.controller.request.FindLinkDomainLinksRequest;
import com.seong.shoutlink.domain.link.linkdomain.controller.request.FindLinkDomainsRequest;
import com.seong.shoutlink.domain.link.linkdomain.controller.request.FindRootDomainsRequest;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainLinksCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainDetailResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainLinksResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.LinkDomainUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/domains")
public class LinkDomainController {

    private final LinkDomainUseCase linkDomainUseCase;

    @GetMapping("/search")
    public ResponseEntity<FindRootDomainsResponse> findRootDomains(
        @ModelAttribute @Valid FindRootDomainsRequest request) {
        FindRootDomainsResponse response = linkDomainUseCase.findRootDomains(
            new FindRootDomainsCommand(request.keyword(), request.size()));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<FindLinkDomainsResponse> findDomains(
        @ModelAttribute @Valid FindLinkDomainsRequest request) {
        FindLinkDomainsResponse response = linkDomainUseCase.findLinkDomains(new FindLinkDomainsCommand(
            request.keyword(), request.page(), request.size()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{domainId}")
    public ResponseEntity<FindLinkDomainDetailResponse> findDomain(
        @PathVariable("domainId") Long domainId) {
        FindLinkDomainDetailResponse response
            = linkDomainUseCase.findLinkDomain(new FindLinkDomainCommand(domainId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{domainId}/links")
    public ResponseEntity<FindLinkDomainLinksResponse> findDomainLinks(
        @PathVariable("domainId") Long domainId,
        @ModelAttribute @Valid FindLinkDomainLinksRequest request) {
        FindLinkDomainLinksResponse response = linkDomainUseCase.findLinkDomainLinks(
            new FindLinkDomainLinksCommand(domainId, request.page(), request.size()));
        return ResponseEntity.ok(response);
    }
}
