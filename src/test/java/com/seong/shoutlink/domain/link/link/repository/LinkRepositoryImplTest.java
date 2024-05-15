package com.seong.shoutlink.domain.link.link.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.base.BaseRepositoryTest;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLink;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.repository.LinkBundleEntity;
import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainCacheRepository;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainEntity;
import com.seong.shoutlink.domain.link.linkdomain.util.DomainExtractor;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.MemberEntity;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LinkRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private LinkRepositoryImpl linkRepository;

    @Autowired
    private LinkDomainCacheRepository linkDomainCacheRepository;

    @Nested
    @DisplayName("save 호출 시")
    class SaveTest {

        @Test
        @DisplayName("성공: 링크 도메인이 없으면 생성된다.")
        void createLinkDomainIfAbsent() {
            //given
            Link link = LinkFixture.link();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            LinkBundleAndLink linkBundleAndLink = new LinkBundleAndLink(link, linkBundle);
            Member member = MemberFixture.member();
            MemberEntity memberEntity = MemberEntity.create(member);
            LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(linkBundle, memberEntity);

            em.persist(memberEntity);
            em.persist(linkBundleEntity);

            //when
            Long savedLinkId = linkRepository.save(linkBundleAndLink);

            //then
            LinkEntity linkEntity = em.find(LinkEntity.class, savedLinkId);
            LinkDomainEntity linkDomainEntity = em.find(LinkDomainEntity.class,
                linkEntity.getLinkDomain().getLinkDomainId());
            assertThat(linkDomainEntity).isNotNull()
                .extracting(LinkDomainEntity::getRootDomain)
                .isEqualTo(DomainExtractor.extractRootDomain(link.getUrl()));
        }

        @Test
        @DisplayName("성공: 링크 도메인이 있으면 생성되지 않는다.")
        void doseNotCreateLinkDomainIfNotAbsent() {
            //given
            Link link = LinkFixture.link();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            LinkBundleAndLink linkBundleAndLink = new LinkBundleAndLink(link, linkBundle);
            Member member = MemberFixture.member();
            MemberEntity memberEntity = MemberEntity.create(member);
            LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(linkBundle, memberEntity);
            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain(DomainExtractor.extractRootDomain(link.getUrl())));

            em.persist(memberEntity);
            em.persist(linkBundleEntity);
            em.persist(linkDomainEntity);

            //when
            Long linkId = linkRepository.save(linkBundleAndLink);

            //then
            LinkEntity linkEntity = em.find(LinkEntity.class, linkId);
            assertThat(linkEntity.getLinkDomain().getLinkDomainId()).isEqualTo(
                linkDomainEntity.getLinkDomainId());
        }

        @Test
        @DisplayName("성공: 링크 도메인을 캐싱한다.")
        void cachingLinkDomain() {
            //given
            Link link = LinkFixture.link();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            LinkBundleAndLink linkBundleAndLink = new LinkBundleAndLink(link, linkBundle);
            Member member = MemberFixture.member();
            MemberEntity memberEntity = MemberEntity.create(member);
            LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(linkBundle, memberEntity);

            em.persist(memberEntity);
            em.persist(linkBundleEntity);

            //when
            Long savedLinkId = linkRepository.save(linkBundleAndLink);

            //then
            List<String> rootDomains = linkDomainCacheRepository.findRootDomains("", 10);
            assertThat(rootDomains).containsExactly(
                DomainExtractor.extractRootDomain(link.getUrl()));
        }
    }
}