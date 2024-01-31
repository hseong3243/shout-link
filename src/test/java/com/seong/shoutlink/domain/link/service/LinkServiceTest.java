package com.seong.shoutlink.domain.link.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.domain.common.StubEventPublisher;
import com.seong.shoutlink.domain.link.repository.FakeLinkRepository;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.linkbundle.repository.FakeLinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkServiceTest {

    @Nested
    @DisplayName("createLink 메서드 호출 시")
    class CreateLinkTest {

        private LinkService linkService;
        private StubMemberRepository memberRepository;
        private FakeLinkRepository linkRepository;
        private FakeLinkBundleRepository linkBundleRepository;
        private StubEventPublisher eventPublisher;

        @BeforeEach
        void setUp() {
            Member member = MemberFixture.member();
            memberRepository = new StubMemberRepository(member);
            linkRepository = new FakeLinkRepository();
            linkBundleRepository = new FakeLinkBundleRepository(LinkBundleFixture.linkBundle(member));
            eventPublisher = new StubEventPublisher();
            linkService = new LinkService(memberRepository, linkRepository, linkBundleRepository,
                eventPublisher);
        }

        @Test
        @DisplayName("성공: 링크 저장됨")
        void createLink() {
            //given
            CreateLinkCommand command = new CreateLinkCommand(1L,
                1L, "https://hseong.tistory.com/", "내 블로그");

            //when
            CreateLinkResponse response = linkService.createLink(command);

            //then
            assertThat(response.linkId()).isNotNull();
        }

        @Test
        @DisplayName("성공: 링크 생성 이벤트 발행됨")
        void createLink_ThenPublishCreateLinkEvent() {
            //given
            CreateLinkCommand command = new CreateLinkCommand(1L,
                1L, "https://hseong.tistory.com/", "내 블로그");

            //when
            CreateLinkResponse response = linkService.createLink(command);

            //then
            assertThat(eventPublisher.getPublishEventCount()).isEqualTo(1);
        }
    }
}