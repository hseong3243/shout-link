package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class DomainEventListener {

    private final LinkDomainRepositoryImpl domainRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {
        domainRepository.synchronizeRootDomains();
    }
}
