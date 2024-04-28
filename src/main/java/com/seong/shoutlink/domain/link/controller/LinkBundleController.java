package com.seong.shoutlink.domain.link.controller;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.domain.auth.NullableUser;
import com.seong.shoutlink.domain.link.controller.request.CreateLinkBundleRequest;
import com.seong.shoutlink.domain.link.service.LinkBundleUseCase;
import com.seong.shoutlink.domain.link.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.link.service.request.FindHubLinkBundlesCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinkBundlesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LinkBundleController {

    private final LinkBundleUseCase linkBundleUseCase;

    @PostMapping("/link-bundles")
    public ResponseEntity<CreateLinkBundleResponse> createLinkBundle(
        @LoginUser Long memberId,
        @Valid @RequestBody CreateLinkBundleRequest request) {
        CreateLinkBundleResponse response = linkBundleUseCase.createLinkBundle(
            new CreateLinkBundleCommand(
                memberId,
                request.description(),
                request.isDefault()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/link-bundles")
    public ResponseEntity<FindLinkBundlesResponse> findLinkBundles(
        @LoginUser Long memberId) {
        FindLinkBundlesResponse response = linkBundleUseCase.findLinkBundles(
            new FindLinkBundlesCommand(memberId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/hubs/{hubId}/link-bundles")
    public ResponseEntity<CreateLinkBundleResponse> createHubLinkBundle(
        @LoginUser Long memberId,
        @PathVariable("hubId") Long hubId,
        @Valid @RequestBody CreateLinkBundleRequest request) {
        CreateLinkBundleResponse response = linkBundleUseCase.createHubLinkBundle(
            new CreateHubLinkBundleCommand(
                hubId,
                memberId,
                request.description(),
                request.isDefault()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hubs/{hubId}/link-bundles")
    public ResponseEntity<FindLinkBundlesResponse> findHubLinkBundles(
        @NullableUser Long nullableMemberId,
        @PathVariable("hubId") Long hubId) {
        FindLinkBundlesResponse response = linkBundleUseCase.findHubLinkBundles(
            new FindHubLinkBundlesCommand(hubId, nullableMemberId));
        return ResponseEntity.ok(response);
    }
}
