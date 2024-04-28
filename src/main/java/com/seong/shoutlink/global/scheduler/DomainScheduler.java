package com.seong.shoutlink.global.scheduler;

import com.seong.shoutlink.domain.domain.repository.DomainRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class DomainScheduler {

    private final DomainRepositoryImpl domainRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void synchronizeRootDomains() {
        domainRepository.synchronizeRootDomains();
    }
}
