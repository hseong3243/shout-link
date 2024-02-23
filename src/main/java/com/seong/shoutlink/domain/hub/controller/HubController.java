package com.seong.shoutlink.domain.hub.controller;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.domain.hub.controller.request.CreateHubRequest;
import com.seong.shoutlink.domain.hub.controller.request.FindHubsRequest;
import com.seong.shoutlink.domain.hub.service.HubService;
import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.request.FindHubCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubDetailResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubsCommand;
import com.seong.shoutlink.domain.hub.service.response.FindHubsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class HubController {

    private final HubService hubService;

    @PostMapping("/hubs")
    public ResponseEntity<CreateHubResponse> createHub(
        @RequestBody @Valid CreateHubRequest request,
        @LoginUser Long memberId) {
        CreateHubResponse response = hubService.createHub(new CreateHubCommand(
            memberId,
            request.name(),
            request.description(),
            request.isPrivate()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hubs")
    public ResponseEntity<FindHubsResponse> findHubs(
        @Valid @ModelAttribute FindHubsRequest request) {
        FindHubsResponse response = hubService.findHubs(new FindHubsCommand(
            request.page(),
            request.size()
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hubs/{hubId}")
    public ResponseEntity<FindHubDetailResponse> findHub(@PathVariable("hubId") Long hubId) {
        FindHubDetailResponse response = hubService.findHub(new FindHubCommand(hubId));
        return ResponseEntity.ok(response);
    }
}
