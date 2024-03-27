package com.seong.shoutlink.global.scheduler;

import com.seong.shoutlink.domain.domain.service.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class DomainScheduler {

    private final DomainRepository domainRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void synchronizeRootDomains() {
        domainRepository.synchronizeRootDomains();
    }
}
