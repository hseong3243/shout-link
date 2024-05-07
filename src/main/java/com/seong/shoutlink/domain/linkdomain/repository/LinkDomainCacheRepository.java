package com.seong.shoutlink.domain.linkdomain.repository;

import java.util.List;

public interface LinkDomainCacheRepository {

    List<String> findRootDomains(String prefix, int count);

    void insert(String rootDomain);
}
