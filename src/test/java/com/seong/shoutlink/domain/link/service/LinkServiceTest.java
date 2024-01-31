package com.seong.shoutlink.domain.link.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.domain.common.StubEventPublisher;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.repository.FakeLinkRepository;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinksResponse;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.repository.FakeLinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkServiceTest {

    private StubMemberRepository memberRepository;
    private FakeLinkRepository linkRepository;
    private FakeLinkBundleRepository linkBundleRepository;
    private StubEventPublisher eventPublisher;

    @Nested
    @DisplayName("createLink 메서드 호출 시")
    class CreateLinkTest {

        private LinkService linkService;
        private LinkBundle stubLinkBundle;

        @BeforeEach
        void setUp() {
            Member member = MemberFixture.member();
            stubLinkBundle = LinkBundleFixture.linkBundle(member);
            memberRepository = new StubMemberRepository(member);
            linkRepository = new FakeLinkRepository();
            linkBundleRepository = new FakeLinkBundleRepository(stubLinkBundle);
            eventPublisher = new StubEventPublisher();
            linkService = new LinkService(memberRepository, linkRepository, linkBundleRepository,
                eventPublisher);
        }

        @Test
        @DisplayName("성공: 링크 저장됨")
        void createLink() {
            //given
            CreateLinkCommand command = new CreateLinkCommand(
                1L,
                stubLinkBundle.getLinkBundleId(),
                "https://hseong.tistory.com/",
                "내 블로그");

            //when
            CreateLinkResponse response = linkService.createLink(command);

            //then
            assertThat(response.linkId()).isNotNull();
        }

        @Test
        @DisplayName("성공: 링크 생성 이벤트 발행됨")
        void createLink_ThenPublishCreateLinkEvent() {
            //given
            CreateLinkCommand command = new CreateLinkCommand(
                1L,
                stubLinkBundle.getLinkBundleId(),
                "https://hseong.tistory.com/",
                "내 블로그");

            //when
            CreateLinkResponse response = linkService.createLink(command);

            //then
            assertThat(eventPublisher.getPublishEventCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("findLinks 메서드 호출 시")
    class FindLinksTest {

        private LinkService linkService;
        private Member stubMember;
        private LinkBundle stubLinkBundle;
        private Link stubLink;

        @BeforeEach
        void setUp() {
            stubMember = MemberFixture.member();
            stubLinkBundle = LinkBundleFixture.linkBundle(stubMember);
            stubLink = LinkFixture.link();
            memberRepository = new StubMemberRepository(stubMember);
            linkRepository = new FakeLinkRepository(stubLink);
            linkBundleRepository = new FakeLinkBundleRepository(stubLinkBundle);
            eventPublisher = new StubEventPublisher();
            linkService = new LinkService(memberRepository, linkRepository, linkBundleRepository,
                eventPublisher);
        }

        @Test
        @DisplayName("성공: 링크 목록 조회됨")
        void findLinks() {
            //given
            FindLinksCommand command = new FindLinksCommand(
                stubMember.getMemberId(),
                stubLinkBundle.getLinkBundleId(),
                0,
                20);

            //when
            FindLinksResponse response = linkService.findLinks(command);

            //then
            assertThat(response.totalElements()).isEqualTo(1);
            assertThat(response.links()).hasSize(1)
                .allSatisfy(link -> {
                    assertThat(link.linkId()).isEqualTo(stubLink.getLinkId());
                    assertThat(link.description()).isEqualTo(stubLink.getDescription());
                    assertThat(link.url()).isEqualTo(stubLink.getUrl());
                });
        }
    }
}
