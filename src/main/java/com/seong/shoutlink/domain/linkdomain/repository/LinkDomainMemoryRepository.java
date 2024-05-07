package com.seong.shoutlink.domain.linkdomain.repository;

import com.seong.shoutlink.domain.common.Trie;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LinkDomainMemoryRepository implements LinkDomainCacheRepository {

    private final Trie trie = new Trie();

    @Override
    public List<String> findRootDomains(String prefix, int count) {
        return trie.search(prefix, count);
    }

    @Override
    public void insert(String rootDomain) {
        trie.insert(rootDomain);
    }
}
