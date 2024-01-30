package com.seong.shoutlink.domain.linkbundle.controller;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.domain.linkbundle.controller.request.CreateLinkBundleRequest;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleService;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LinkBundleController {

    private final LinkBundleService linkBundleService;

    @PostMapping("/link-bundles")
    public ResponseEntity<CreateLinkBundleResponse> createLinkBundle(
        @LoginUser Long memberId,
        @Valid @RequestBody CreateLinkBundleRequest request) {
        CreateLinkBundleResponse response = linkBundleService.createLinkBundle(
            new CreateLinkBundleCommand(
                memberId,
                request.description(),
                request.isDefault()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
