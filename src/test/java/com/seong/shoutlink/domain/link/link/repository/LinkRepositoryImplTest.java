package com.seong.shoutlink.domain.link.link.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.base.BaseRepositoryTest;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLink;
import com.seong.shoutlink.domain.link.link.service.LinkOrderBy;
import com.seong.shoutlink.domain.link.link.service.result.LinkPaginationResult;
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
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

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

        @Test
        @DisplayName("예외(illegalStateEx): 존재하지 않는 링크 묶음 조회 시")
        void illegalStateEx_WhenTryToFindLinkBundle_DoseNotExist() {
            //given
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            Link link = LinkFixture.link();
            LinkBundleAndLink linkBundleAndLink = new LinkBundleAndLink(link, linkBundle);

            //when
            Exception exception = catchException(() -> linkRepository.save(linkBundleAndLink));

            //then
            assertThat(exception).isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("findLinks 호출 시")
    class FindLinksTest {

        private MemberEntity memberEntity;
        private LinkBundleEntity linkBundleEntity;

        @BeforeEach
        void setUp() {
            memberEntity = MemberEntity.create(MemberFixture.member());
            linkBundleEntity = LinkBundleEntity.create(LinkBundleFixture.linkBundle(),
                memberEntity);
            em.persist(memberEntity);
            em.persist(linkBundleEntity);
        }

        @Test
        @DisplayName("성공: 정렬 기준이 createdAt인 경우 생성일 기준으로 내림차순 정렬된다.")
        void findLinks_OrderByCreatedAt() {
            //given
            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain("github.com"));
            LinkEntity linkEntityA = LinkEntity.create(new Link("github.com", "깃허브", null),
                linkBundleEntity, linkDomainEntity);
            LinkEntity linkEntityB = LinkEntity.create(new Link("github.com", "깃허브", null),
                linkBundleEntity, linkDomainEntity);
            LinkEntity linkEntityC = LinkEntity.create(new Link("github.com", "깃허브", null),
                linkBundleEntity, linkDomainEntity);

            LocalDateTime now = LocalDateTime.now();
            ReflectionTestUtils.setField(linkEntityB, "createdAt", now.plusDays(1));
            ReflectionTestUtils.setField(linkEntityC, "createdAt", now.plusDays(2));

            em.persist(linkDomainEntity);
            em.persist(linkEntityA);
            em.persist(linkEntityB);
            em.persist(linkEntityC);

            //when
            LinkBundle linkBundle = linkBundleEntity.toDomain();
            LinkOrderBy orderByCreatedAt = LinkOrderBy.CREATED_AT;
            LinkPaginationResult result = linkRepository.findLinks(linkBundle, 0, 10,
                orderByCreatedAt);

            //then
            List<Link> links = result.links();
            assertThat(links).extracting(Link::getLinkId)
                .containsExactly(
                    linkEntityC.getLinkId(),
                    linkEntityB.getLinkId(),
                    linkEntityA.getLinkId());
        }

        @Test
        @DisplayName("성공: 정렬 기준이 expiredAt인 경우 만료일 기준으로 내림차순 정렬된다.")
        void findLinks_OrderByExpiredAt() {
            //given
            LocalDateTime expiredAt = LocalDateTime.of(2023, 6, 8, 12, 0);

            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain("github.com"));
            LinkEntity linkEntityA = LinkEntity.create(
                new Link("github.com", "깃허브", expiredAt),
                linkBundleEntity, linkDomainEntity);
            LinkEntity linkEntityB = LinkEntity.create(
                new Link("github.com", "깃허브", expiredAt.plusDays(1)),
                linkBundleEntity, linkDomainEntity);
            LinkEntity linkEntityC = LinkEntity.create(
                new Link("github.com", "깃허브", expiredAt.plusDays(2)),
                linkBundleEntity, linkDomainEntity);

            em.persist(linkDomainEntity);
            em.persist(linkEntityA);
            em.persist(linkEntityB);
            em.persist(linkEntityC);

            //when
            LinkBundle linkBundle = linkBundleEntity.toDomain();
            LinkOrderBy orderByCreatedAt = LinkOrderBy.EXPIRED_AT;
            LinkPaginationResult result = linkRepository.findLinks(linkBundle, 0, 10,
                orderByCreatedAt);

            //then
            List<Link> links = result.links();
            assertThat(links).extracting(Link::getLinkId)
                .containsExactly(
                    linkEntityC.getLinkId(),
                    linkEntityB.getLinkId(),
                    linkEntityA.getLinkId());
        }
    }
}
