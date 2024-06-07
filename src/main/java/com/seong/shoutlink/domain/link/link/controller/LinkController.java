package com.seong.shoutlink.domain.link.link.controller;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.domain.auth.NullableUser;
import com.seong.shoutlink.domain.link.link.controller.request.CreateLinkRequest;
import com.seong.shoutlink.domain.link.link.controller.request.FindLinksRequest;
import com.seong.shoutlink.domain.link.link.service.LinkUseCase;
import com.seong.shoutlink.domain.link.link.service.request.CreateHubLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.DeleteHubLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.DeleteLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.FindHubLinksCommand;
import com.seong.shoutlink.domain.link.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.link.service.response.CreateHubLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.DeleteLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.FindLinksResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LinkController {

    private final LinkUseCase linkUseCase;

    @PostMapping("/links")
    public ResponseEntity<CreateLinkResponse> createLink(
        @LoginUser Long memberId,
        @Valid @RequestBody CreateLinkRequest request) {
        CreateLinkResponse response = linkUseCase.createLink(new CreateLinkCommand(
            memberId,
            request.linkBundleId(),
            request.url(),
            request.description(),
            request.expiredAt()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/links")
    public ResponseEntity<FindLinksResponse> findLinks(
        @LoginUser Long memberId,
        @Valid @ModelAttribute FindLinksRequest request) {
        FindLinksResponse response = linkUseCase.findLinks(new FindLinksCommand(
            memberId,
            request.linkBundleId(),
            request.page(),
            request.size()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/hubs/{hubId}/links")
    public ResponseEntity<CreateHubLinkResponse> createHubLink(
        @PathVariable("hubId") Long hubId,
        @LoginUser Long memberId,
        @Valid @RequestBody CreateLinkRequest request) {
        CreateHubLinkResponse response = linkUseCase.createHubLink(new CreateHubLinkCommand(
            hubId,
            memberId,
            request.linkBundleId(),
            request.url(),
            request.description(),
            request.expiredAt()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hubs/{hubId}/links")
    public ResponseEntity<FindLinksResponse> findHubLinks(
        @PathVariable("hubId") Long hubId,
        @NullableUser Long nullableMemberId,
        @Valid @ModelAttribute FindLinksRequest request) {
        FindLinksResponse response = linkUseCase.findHubLinks(new FindHubLinksCommand(
            request.linkBundleId(),
            hubId,
            nullableMemberId,
            request.page(),
            request.size()));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/links/{linkId}")
    public ResponseEntity<DeleteLinkResponse> deleteLink(
        @LoginUser Long memberId,
        @PathVariable("linkId") Long linkId) {
        DeleteLinkResponse deleteLinkResponse = linkUseCase.deleteLink(
            new DeleteLinkCommand(memberId, linkId));
        return ResponseEntity.ok(deleteLinkResponse);
    }

    @DeleteMapping("/hubs/{hubId}/links/{linkId}")
    public ResponseEntity<DeleteLinkResponse> deleteHubLink(
        @LoginUser Long memberId,
        @PathVariable("hubId") Long hubId,
        @PathVariable("linkId") Long linkId) {
        DeleteLinkResponse response = linkUseCase.deleteHubLink(
            new DeleteHubLinkCommand(linkId, memberId, hubId));
        return ResponseEntity.ok(response);
    }
}