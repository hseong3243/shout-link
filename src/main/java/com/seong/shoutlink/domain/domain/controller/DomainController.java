package com.seong.shoutlink.domain.domain.controller;

import com.seong.shoutlink.domain.domain.controller.request.FindDomainsRequest;
import com.seong.shoutlink.domain.domain.controller.request.FindRootDomainsRequest;
import com.seong.shoutlink.domain.domain.service.DomainService;
import com.seong.shoutlink.domain.domain.service.request.FindDomainCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.domain.service.response.FindDomainDetailResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.FindRootDomainsResponse;
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
public class DomainController {

    private final DomainService domainService;

    @GetMapping("/search")
    public ResponseEntity<FindRootDomainsResponse> findRootDomains(
        @ModelAttribute @Valid FindRootDomainsRequest request) {
        FindRootDomainsResponse response = domainService.findRootDomains(
            new FindRootDomainsCommand(request.keyword(), request.size()));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<FindDomainsResponse> findDomains(
        @ModelAttribute @Valid FindDomainsRequest request) {
        FindDomainsResponse response = domainService.findDomains(new FindDomainsCommand(
            request.keyword(), request.page(), request.size()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{domainId}")
    public ResponseEntity<FindDomainDetailResponse> findDomain(
        @PathVariable("domainId") Long domainId) {
        FindDomainDetailResponse response
            = domainService.findDomain(new FindDomainCommand(domainId));
        return ResponseEntity.ok(response);
    }
}
