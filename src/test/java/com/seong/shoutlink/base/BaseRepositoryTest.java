package com.seong.shoutlink.base;

import com.seong.shoutlink.base.BaseRepositoryTest.CacheConfig;
import com.seong.shoutlink.base.BaseRepositoryTest.RepositoryConfig;
import com.seong.shoutlink.domain.hub.repository.HubJpaRepository;
import com.seong.shoutlink.domain.hub.repository.HubMemberJpaRepository;
import com.seong.shoutlink.domain.hub.repository.HubRepositoryImpl;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.link.link.repository.LinkJpaRepository;
import com.seong.shoutlink.domain.link.link.repository.LinkRepositoryImpl;
import com.seong.shoutlink.domain.link.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.linkbundle.repository.LinkBundleJpaRepository;
import com.seong.shoutlink.domain.link.linkbundle.repository.LinkBundleRepositoryImpl;
import com.seong.shoutlink.domain.link.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainCacheRepository;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainJpaRepository;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainMemoryRepository;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainRepositoryImpl;
import com.seong.shoutlink.domain.link.linkdomain.service.LinkDomainRepository;
import com.seong.shoutlink.domain.member.repository.MemberJpaRepository;
import com.seong.shoutlink.domain.member.repository.MemberRepositoryImpl;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import com.seong.shoutlink.domain.tag.repository.TagJpaRepository;
import com.seong.shoutlink.domain.tag.repository.TagRepositoryImpl;
import com.seong.shoutlink.domain.tag.service.TagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({RepositoryConfig.class, CacheConfig.class, DatabaseCleaner.class})
public abstract class BaseRepositoryTest {

    @TestConfiguration
    static class RepositoryConfig {

        @Autowired
        MemberJpaRepository memberJpaRepository;

        @Autowired
        HubJpaRepository hubJpaRepository;

        @Autowired
        HubMemberJpaRepository hubMemberJpaRepository;

        @Autowired
        LinkBundleJpaRepository linkBundleJpaRepository;

        @Autowired
        LinkJpaRepository linkJpaRepository;

        @Autowired
        LinkDomainJpaRepository linkDomainJpaRepository;

        @Autowired
        TagJpaRepository tagJpaRepository;

        @Bean
        public MemberRepository memberRepository() {
            return new MemberRepositoryImpl(memberJpaRepository);
        }

        @Bean
        public HubRepository hubRepository() {
            return new HubRepositoryImpl(memberJpaRepository, hubJpaRepository,
                hubMemberJpaRepository);
        }

        @Bean
        public LinkRepository linkRepository(LinkDomainCacheRepository linkDomainCacheRepository) {
            return new LinkRepositoryImpl(linkBundleJpaRepository, linkJpaRepository,
                linkDomainJpaRepository, linkDomainCacheRepository);
        }

        @Bean
        public LinkBundleRepository linkBundleRepository() {
            return new LinkBundleRepositoryImpl(linkBundleJpaRepository, memberJpaRepository,
                hubJpaRepository);
        }

        @Bean
        public LinkDomainRepository linkDomainRepository(LinkDomainCacheRepository linkDomainCacheRepository) {
            return new LinkDomainRepositoryImpl(linkDomainJpaRepository, linkDomainCacheRepository,
                linkJpaRepository);
        }

        @Bean
        public TagRepository tagRepository() {
            return new TagRepositoryImpl(tagJpaRepository, hubJpaRepository, memberJpaRepository);
        }
    }

    @TestConfiguration
    static class CacheConfig {

        @Bean
        public LinkDomainCacheRepository linkDomainCacheRepository() {
            return new LinkDomainMemoryRepository();
        }
    }

    @Autowired
    protected EntityManager em;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }
}
