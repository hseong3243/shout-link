package com.seong.shoutlink.domain.link.controller;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.domain.link.controller.request.CreateLinkRequest;
import com.seong.shoutlink.domain.link.controller.request.FindLinksRequest;
import com.seong.shoutlink.domain.link.service.LinkService;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinksResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/links")
    public ResponseEntity<CreateLinkResponse> createLink(
        @LoginUser Long memberId,
        @Valid @RequestBody CreateLinkRequest request) {
        CreateLinkResponse response = linkService.createLink(new CreateLinkCommand(
            memberId,
            request.linkBundleId(),
            request.url(),
            request.description()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/links")
    public ResponseEntity<FindLinksResponse> findLinks(
        @LoginUser Long memberId,
        @Valid @ModelAttribute FindLinksRequest request) {
        FindLinksResponse response = linkService.findLinks(new FindLinksCommand(
            memberId,
            request.linkBundleId(),
            request.page(),
            request.size()));
        return ResponseEntity.ok(response);
    }
}
