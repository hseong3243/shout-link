package com.seong.shoutlink.global.config;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.domain.service.DomainService;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleService;
import com.seong.shoutlink.global.event.DomainEventListener;
import com.seong.shoutlink.global.event.LinkBundleEventListener;
import com.seong.shoutlink.global.event.SpringEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringEventPublisher(applicationEventPublisher);
    }

    @Bean
    public LinkBundleEventListener springEventListener(LinkBundleService linkBundleService) {
        return new LinkBundleEventListener(linkBundleService);
    }

    @Bean
    public DomainEventListener domainEventListener(DomainService domainService) {
        return new DomainEventListener(domainService);
    }
}
