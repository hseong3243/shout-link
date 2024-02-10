package com.seong.shoutlink.global.config;

import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleService;
import com.seong.shoutlink.global.event.SpringEventListener;
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
    public SpringEventListener springEventListener(LinkBundleService linkBundleService) {
        return new SpringEventListener(linkBundleService);
    }
}