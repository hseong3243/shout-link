package com.seong.shoutlink.global.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.seong.shoutlink.base.BaseIntegrationTest;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.repository.HubEntity;
import com.seong.shoutlink.domain.hub.repository.HubMemberEntity;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.repository.LinkEntity;
import com.seong.shoutlink.domain.link.link.service.LinkUseCase;
import com.seong.shoutlink.domain.link.link.service.request.CreateHubLinkCommand;
import com.seong.shoutlink.domain.link.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.repository.LinkBundleEntity;
import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainEntity;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.MemberEntity;
import com.seong.shoutlink.domain.tag.repository.TagEntity;
import com.seong.shoutlink.domain.tag.service.AutoGenerativeClient;
import com.seong.shoutlink.domain.tag.service.ai.GeneratedTag;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import com.seong.shoutlink.global.client.api.ApiException;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TagEventListenerTest extends BaseIntegrationTest {

    @MockBean
    private AutoGenerativeClient autoGenerativeClient;

    @Autowired
    private LinkUseCase linkService;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("허브 링크 생성 이벤트 발행 시")
    class CreateHubLinkEvent_Published {

        private MemberEntity memberEntity;
        private HubEntity hubEntity;
        private HubMemberEntity hubMemberEntity;
        private LinkBundleEntity linkBundleEntity;

        @BeforeEach
        void setUp() {
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            memberEntity = MemberEntity.create(member);
            hubEntity = HubEntity.create(hub);
            hubMemberEntity = HubMemberEntity.create(memberEntity, hubEntity);
            linkBundleEntity = LinkBundleEntity.create(linkBundle, hubEntity);

            persist(memberEntity, hubEntity, hubMemberEntity, linkBundleEntity);
        }

        @Test
        @DisplayName("성공: 허브 태그 자동 생성을 호출한다.")
        void createHubTags() {
            //given
            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain("github.com"));
            Link link = new Link("https://github.com", "asdf");
            persist(
                linkDomainEntity,
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity)
            );

            CreateHubLinkCommand command = new CreateHubLinkCommand(
                hubEntity.getHubId(),
                memberEntity.getMemberId(),
                linkBundleEntity.getLinkBundleId(),
                "https://github.com/hseong3243",
                "내 깃허브");
            List<GeneratedTag> stubbedResponse = Stream.of("태그A", "태그B", "태그C")
                .map(GeneratedTag::new)
                .toList();

            given(autoGenerativeClient.generateTags(any()))
                .willReturn(stubbedResponse);

            //when
            linkService.createHubLink(command);

            //then
            Awaitility.await().untilAsserted(() -> {
                List<TagEntity> tags = em.createQuery("select t from TagEntity t",
                        TagEntity.class)
                    .getResultList();
                assertThat(tags)
                    .extracting(TagEntity::getName)
                    .containsExactlyInAnyOrder("태그A", "태그B", "태그C");
            });
        }

        @Test
        @DisplayName("예외(apiException): apiException 발생시 3회까지 재시도 한다.")
        void retry_whenApiException() {
            //given
            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain("github.com"));
            Link link = new Link("https://github.com", "asdf");
            persist(
                linkDomainEntity,
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity)
            );

            CreateHubLinkCommand command = new CreateHubLinkCommand(
                hubEntity.getHubId(),
                memberEntity.getMemberId(),
                linkBundleEntity.getLinkBundleId(),
                "https://github.com/hseong3243",
                "내 깃허브");

            given(autoGenerativeClient.generateTags(any())).willThrow(ApiException.class);

            //when
            linkService.createHubLink(command);

            //then
            Awaitility.await().untilAsserted(() -> {
                then(autoGenerativeClient).should(times(3)).generateTags(any());
            });
        }
    }

    @Nested
    @DisplayName("회원 링크 생성 이벤트 발생 시")
    class MemberCreateLinkEvent_Published {


        private MemberEntity memberEntity;
        private LinkBundleEntity linkBundleEntity;

        @BeforeEach
        void setUp() {
            Member member = MemberFixture.member();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            memberEntity = MemberEntity.create(member);
            linkBundleEntity = LinkBundleEntity.create(linkBundle, memberEntity);

            persist(memberEntity, linkBundleEntity);
        }

        @Test
        @DisplayName("성공: 허브 태그 자동 생성을 호출한다.")
        void createHubTags() {
            //given
            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain("github.com"));
            Link link = new Link("https://github.com", "asdf");
            persist(
                linkDomainEntity,
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity)
            );

            CreateLinkCommand command = new CreateLinkCommand(
                memberEntity.getMemberId(),
                linkBundleEntity.getLinkBundleId(),
                "https://github.com/hseong3243",
                "내 깃허브");
            List<GeneratedTag> stubbedResponse = Stream.of("태그A", "태그B", "태그C")
                .map(GeneratedTag::new)
                .toList();

            given(autoGenerativeClient.generateTags(any()))
                .willReturn(stubbedResponse);

            //when
            linkService.createLink(command);

            //then
            Awaitility.await().untilAsserted(() -> {
                List<TagEntity> tags = em.createQuery("select t from TagEntity t",
                        TagEntity.class)
                    .getResultList();
                assertThat(tags)
                    .extracting(TagEntity::getName)
                    .containsExactlyInAnyOrder("태그A", "태그B", "태그C");
            });
        }

        @Test
        @DisplayName("예외(apiException): apiException 발생시 3회까지 재시도 한다.")
        void retry_whenApiException() {
            //given
            LinkDomainEntity linkDomainEntity = LinkDomainEntity.create(
                new LinkDomain("github.com"));
            Link link = new Link("https://github.com", "asdf");
            persist(
                linkDomainEntity,
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity),
                LinkEntity.create(link, linkBundleEntity, linkDomainEntity)
            );

            CreateLinkCommand command = new CreateLinkCommand(
                memberEntity.getMemberId(),
                linkBundleEntity.getLinkBundleId(),
                "https://github.com/hseong3243",
                "내 깃허브");

            given(autoGenerativeClient.generateTags(any())).willThrow(ApiException.class);

            //when
            linkService.createLink(command);

            //then
            Awaitility.await().untilAsserted(() -> {
                then(autoGenerativeClient).should(times(3)).generateTags(any());
            });
        }
    }
}
