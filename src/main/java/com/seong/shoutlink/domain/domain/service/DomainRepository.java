package com.seong.shoutlink.domain.domain.service;

import com.seong.shoutlink.domain.domain.Domain;
import java.util.List;
import com.seong.shoutlink.domain.domain.service.result.DomainPaginationResult;
import java.util.Optional;

public interface DomainRepository {

    Optional<Domain> findByRootDomain(String rootDomain);

    Domain save(Domain domain);

    List<String> findRootDomains(String keyword, int size);

    void synchronizeRootDomains();

    DomainPaginationResult findDomains(String keyword, int page, int size);

    Optional<Domain> findById(Long domainId);
}
