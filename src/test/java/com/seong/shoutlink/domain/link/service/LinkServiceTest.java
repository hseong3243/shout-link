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
    private LinkService linkService;

    @BeforeEach
    void setUp() {
        memberRepository = new StubMemberRepository();
        linkRepository = new FakeLinkRepository();
        linkBundleRepository = new FakeLinkBundleRepository();
        eventPublisher = new StubEventPublisher();
        linkService = new LinkService(memberRepository, linkRepository, linkBundleRepository,
            eventPublisher);
    }

    @Nested
    @DisplayName("createLink 메서드 호출 시")
    class CreateLinkTest {

        @Test
        @DisplayName("성공: 링크 저장됨")
        void createLink() {
            //given
            Member member = MemberFixture.member();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            memberRepository.stub(member);
            linkBundleRepository.stub(linkBundle);
            CreateLinkCommand command = new CreateLinkCommand(
                1L,
                linkBundle.getLinkBundleId(),
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
            Member member = MemberFixture.member();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            memberRepository.stub(member);
            linkBundleRepository.stub(linkBundle);
            CreateLinkCommand command = new CreateLinkCommand(
                1L,
                linkBundle.getLinkBundleId(),
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

        @Test
        @DisplayName("성공: 링크 목록 조회됨")
        void findLinks() {
            //given
            Member member = MemberFixture.member();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            Link link = LinkFixture.link();
            memberRepository.stub(member);
            linkBundleRepository.stub(linkBundle);
            linkRepository.stub(link);
            FindLinksCommand command = new FindLinksCommand(
                member.getMemberId(),
                linkBundle.getLinkBundleId(),
                0,
                20);

            //when
            FindLinksResponse response = linkService.findLinks(command);

            //then
            assertThat(response.totalElements()).isEqualTo(1);
            assertThat(response.links()).hasSize(1)
                .allSatisfy(findLink -> {
                    assertThat(findLink.linkId()).isEqualTo(link.getLinkId());
                    assertThat(findLink.description()).isEqualTo(link.getDescription());
                    assertThat(findLink.url()).isEqualTo(link.getUrl());
                });
        }
    }
}
