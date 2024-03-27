package com.seong.shoutlink.domain.domain.repository;

import java.util.List;

public interface DomainCacheRepository {

    List<String> findRootDomains(String prefix, int count);
}
