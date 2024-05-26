package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.hub.service.event.CreateHubEvent;
import com.seong.shoutlink.domain.link.linkbundle.service.LinkBundleUseCase;
import com.seong.shoutlink.domain.link.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.link.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.link.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.member.service.event.CreateMemberEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class LinkBundleEventListener {

    private static final String DEFAULT_LINK_BUNDLE = "기본";

    private final LinkBundleUseCase linkBundleUseCase;

    @Async
    @TransactionalEventListener
    public void createDefaultLinkBundle(CreateMemberEvent event) {
        CreateLinkBundleCommand command
            = new CreateLinkBundleCommand(event.memberId(), DEFAULT_LINK_BUNDLE, true);
        CreateLinkBundleResponse response = linkBundleUseCase.createLinkBundle(command);
        log.debug("[Event] 회원 기본 링크 번들 생성. linkBundleId={}", response.linkBundleId());
    }

    @Async
    @TransactionalEventListener
    public void createDefaultHubLinkBundle(CreateHubEvent event) {
        CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(
            event.hubId(),
            event.memberId(),
            DEFAULT_LINK_BUNDLE,
            true);
        CreateLinkBundleResponse response = linkBundleUseCase.createHubLinkBundle(command);
        log.debug("[Event] 허브 기본 링크 번들 생성. linkBundleId={}", response.linkBundleId());
    }
}
