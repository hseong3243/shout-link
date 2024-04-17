package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.domain.service.DomainUseCase;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.link.service.event.CreateLinkEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
public class DomainEventListener {

    private final DomainUseCase domainUseCase;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateDomainInfo(CreateLinkEvent event) {
        domainUseCase.updateDomain(new UpdateDomainCommand(event.linkId(), event.url()));
    }
}
