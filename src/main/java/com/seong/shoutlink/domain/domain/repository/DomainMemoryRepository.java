package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.common.Trie;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DomainMemoryRepository implements DomainCacheRepository {

    private final Trie trie = new Trie();

    @Override
    public List<String> findRootDomains(String prefix, int count) {
        return trie.search(prefix, count);
    }
}
