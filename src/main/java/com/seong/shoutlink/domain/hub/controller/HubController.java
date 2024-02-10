package com.seong.shoutlink.domain.hub.controller;

import com.seong.shoutlink.domain.auth.LoginUser;
import com.seong.shoutlink.domain.hub.controller.request.CreateHubRequest;
import com.seong.shoutlink.domain.hub.service.HubService;
import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
}
