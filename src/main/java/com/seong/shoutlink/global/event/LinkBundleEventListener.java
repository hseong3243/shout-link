package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.auth.service.event.CreateMemberEvent;
import com.seong.shoutlink.domain.hub.service.event.CreateHubEvent;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleService;
import com.seong.shoutlink.domain.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
public class LinkBundleEventListener {

    private static final String DEFAULT_LINK_BUNDLE = "기본";

    private final LinkBundleService linkBundleService;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createDefaultLinkBundle(CreateMemberEvent event) {
        CreateLinkBundleCommand command
            = new CreateLinkBundleCommand(event.memberId(), DEFAULT_LINK_BUNDLE, true);
        linkBundleService.createLinkBundle(command);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createDefaultHubLinkBundle(CreateHubEvent event) {
        CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(
            event.hubId(),
            event.memberId(),
            DEFAULT_LINK_BUNDLE,
            true);
        linkBundleService.createHubLinkBundle(command);
    }
}
