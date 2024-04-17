package com.seong.shoutlink.global.config;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.domain.service.DomainUseCase;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleUseCase;
import com.seong.shoutlink.domain.tag.service.TagUseCase;
import com.seong.shoutlink.global.event.DomainEventListener;
import com.seong.shoutlink.global.event.LinkBundleEventListener;
import com.seong.shoutlink.global.event.SpringEventPublisher;
import com.seong.shoutlink.global.event.TagEventListener;
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
    public LinkBundleEventListener linkBundleEventListener(LinkBundleUseCase linkBundleUseCase) {
        return new LinkBundleEventListener(linkBundleUseCase);
    }

    @Bean
    public DomainEventListener domainEventListener(DomainUseCase domainUseCase) {
        return new DomainEventListener(domainUseCase);
    }

    @Bean
    public TagEventListener tagEventListener(TagUseCase tagUseCase) {
        return new TagEventListener(tagUseCase);
    }
}
