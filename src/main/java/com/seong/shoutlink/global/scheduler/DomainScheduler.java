package com.seong.shoutlink.global.scheduler;

import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class DomainScheduler {

    private final LinkDomainRepositoryImpl domainRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void synchronizeRootDomains() {
        domainRepository.synchronizeRootDomains();
    }
}
