package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.domain.service.DomainService;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.link.service.event.CreateLinkEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
public class DomainEventListener {

    private final DomainService domainService;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateDomainInfo(CreateLinkEvent event) {
        domainService.updateDomain(new UpdateDomainCommand(event.linkId(), event.url()));
    }
}
