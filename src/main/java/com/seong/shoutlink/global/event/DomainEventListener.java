package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainRepositoryImpl;
import com.seong.shoutlink.domain.link.linkdomain.service.LinkDomainUseCase;
import com.seong.shoutlink.domain.link.linkdomain.service.request.UpdateLinkDomainCommand;
import com.seong.shoutlink.domain.link.service.event.CreateLinkEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
public class DomainEventListener {

    private final LinkDomainUseCase linkDomainUseCase;
    private final LinkDomainRepositoryImpl domainRepository;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateDomainInfo(CreateLinkEvent event) {
        linkDomainUseCase.updateLinkDomain(new UpdateLinkDomainCommand(event.linkId(), event.url()));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {
        domainRepository.synchronizeRootDomains();
    }
}
