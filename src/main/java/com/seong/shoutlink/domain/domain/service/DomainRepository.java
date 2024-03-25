package com.seong.shoutlink.domain.domain.service;

import com.seong.shoutlink.domain.domain.Domain;
import java.util.Optional;

public interface DomainRepository {

    Optional<Domain> findByRootDomain(String rootDomain);

    Domain save(Domain domain);
}
