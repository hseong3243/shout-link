package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.member.service.event.CreateMemberEvent;
import com.seong.shoutlink.domain.hub.service.event.CreateHubEvent;
import com.seong.shoutlink.domain.link.linkbundle.service.LinkBundleUseCase;
import com.seong.shoutlink.domain.link.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.link.linkbundle.service.response.CreateLinkBundleCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
public class LinkBundleEventListener {

    private static final String DEFAULT_LINK_BUNDLE = "기본";

    private final LinkBundleUseCase linkBundleUseCase;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createDefaultLinkBundle(CreateMemberEvent event) {
        CreateLinkBundleCommand command
            = new CreateLinkBundleCommand(event.memberId(), DEFAULT_LINK_BUNDLE, true);
        linkBundleUseCase.createLinkBundle(command);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createDefaultHubLinkBundle(CreateHubEvent event) {
        CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(
            event.hubId(),
            event.memberId(),
            DEFAULT_LINK_BUNDLE,
            true);
        linkBundleUseCase.createHubLinkBundle(command);
    }
}
