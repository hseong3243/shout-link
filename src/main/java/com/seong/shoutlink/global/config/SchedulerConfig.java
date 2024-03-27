package com.seong.shoutlink.global.config;

import com.seong.shoutlink.domain.domain.service.DomainRepository;
import com.seong.shoutlink.global.scheduler.DomainScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public DomainScheduler domainScheduler(DomainRepository domainRepository) {
        return new DomainScheduler(domainRepository);
    }
}
