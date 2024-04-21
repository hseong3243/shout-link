package com.seong.shoutlink.domain.link.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.common.StubEventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hubmember.repository.StubHubMemberRepository;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.repository.StubLinkRepository;
import com.seong.shoutlink.domain.link.service.request.CreateHubLinkCommand;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.request.DeleteLinkCommand;
import com.seong.shoutlink.domain.link.service.request.FindHubLinksCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinksCommand;
import com.seong.shoutlink.domain.link.service.response.CreateHubLinkResponse;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinksResponse;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.repository.StubLinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import com.seong.shoutlink.domain.hub.repository.StubHubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkServiceTest {

    private StubMemberRepository memberRepository;
    private StubLinkRepository linkRepository;
    private StubLinkBundleRepository linkBundleRepository;
    private StubHubRepository hubRepository;
    private StubHubMemberRepository hubMemberRepository;
    private StubEventPublisher eventPublisher;
    private LinkService linkService;

    @BeforeEach
    void setUp() {
        memberRepository = new StubMemberRepository();
        hubRepository = new StubHubRepository();
        hubMemberRepository = new StubHubMemberRepository();
        linkRepository = new StubLinkRepository();
        linkBundleRepository = new StubLinkBundleRepository();
        eventPublisher = new StubEventPublisher();
        linkService = new LinkService(memberRepository, hubRepository, hubMemberRepository,
            linkRepository, linkBundleRepository, eventPublisher);
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

    @Nested
    @DisplayName("createHubLink 메서드 호출 시")
    class CreateHubLinkTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            hubMemberRepository = new StubHubMemberRepository();
            linkRepository = new StubLinkRepository();
            linkBundleRepository = new StubLinkBundleRepository();
            eventPublisher = new StubEventPublisher();
            linkService = new LinkService(memberRepository, hubRepository, hubMemberRepository,
                linkRepository, linkBundleRepository, eventPublisher);
        }

        @Test
        @DisplayName("성공: 허브 링크 생성됨")
        void createHubLink() {
            //given
            Member member = MemberFixture.member();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            linkBundleRepository.stub(linkBundle);
            hubRepository.stub(hub);
            CreateHubLinkCommand command = new CreateHubLinkCommand(hub.getHubId(),
                hub.getMasterId(), linkBundle.getLinkBundleId(), "url", "설명");

            //when
            CreateHubLinkResponse response = linkService.createHubLink(command);

            //then
            assertThat(response.linkId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("성공: 링크 생성 이벤트 발행됨")
        void createHubLink_ThenPublishCreateLinkEvent() {
            //given
            Member member = MemberFixture.member();
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            linkBundleRepository.stub(linkBundle);
            hubRepository.stub(hub);
            CreateHubLinkCommand command = new CreateHubLinkCommand(hub.getHubId(),
                hub.getMasterId(), linkBundle.getLinkBundleId(), "url", "설명");

            //when
            CreateHubLinkResponse response = linkService.createHubLink(command);

            //then
            assertThat(eventPublisher.getPublishEventCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 허브")
        void notFound_WhenHubNotFound() {
            //given
            CreateHubLinkCommand command = new CreateHubLinkCommand(1L, 1L, 1L, "url", "설명");

            //when
            Exception exception = catchException(() -> linkService.createHubLink(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 링크 묶음")
        void notFound_WhenHubLinkBundleNotFound() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            hubRepository.stub(hub);
            CreateHubLinkCommand command = new CreateHubLinkCommand(hub.getHubId(),
                member.getMemberId(), 1L, "url", "설명");

            //when
            Exception exception = catchException(() -> linkService.createHubLink(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(Unahthorized): 사용자가 허브 마스터가 아님")
        void unauthorized_WhenMemberIsNotMaster() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            hubRepository.stub(hub);
            Long notMasterId = hub.getMasterId() + 1;
            CreateHubLinkCommand command = new CreateHubLinkCommand(hub.getHubId(), notMasterId, 1L,
                "url", "설명");

            //when
            Exception exception = catchException(() -> linkService.createHubLink(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("findHubLinks 메서드 호출 시")
    class FindHubLinksTest {

        private Member member;
        private Hub hub;
        private LinkBundle linkBundle;
        private Link link;

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            hubMemberRepository = new StubHubMemberRepository();
            linkBundleRepository = new StubLinkBundleRepository();
            linkRepository = new StubLinkRepository();
            eventPublisher = new StubEventPublisher();
            linkService = new LinkService(memberRepository, hubRepository, hubMemberRepository,
                linkRepository, linkBundleRepository, eventPublisher);

            member = MemberFixture.member();
            hub = HubFixture.hub(member);
            linkBundle = LinkBundleFixture.linkBundle();
            link = LinkFixture.link();
        }

        @Test
        @DisplayName("성공: 허브 링크 목록 조회됨")
        void findHubLinks() {
            //given
            memberRepository.stub(member);
            hubRepository.stub(hub);
            linkBundleRepository.stub(linkBundle);
            linkRepository.stub(link);

            FindHubLinksCommand command = new FindHubLinksCommand(linkBundle.getLinkBundleId(),
                hub.getHubId(), null, 0, 10);

            //when
            FindLinksResponse response = linkService.findHubLinks(command);

            //then
            assertThat(response.totalElements()).isEqualTo(1);
            assertThat(response.hasNext()).isFalse();
            assertThat(response.links()).hasSize(1)
                .allSatisfy(findLink -> {
                    assertThat(findLink.linkId()).isEqualTo(link.getLinkId());
                    assertThat(findLink.url()).isEqualTo(link.getUrl());
                    assertThat(findLink.description()).isEqualTo(link.getDescription());
                });
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 허브")
        void notFound_WhenHubNotFound() {
            //given
            FindHubLinksCommand command = new FindHubLinksCommand(1L, 1L, null, 0, 10);

            //when
            Exception exception = catchException(() -> linkService.findHubLinks(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 링크 묶음")
        void notFound_WhenLinkBundleNotFound() {
            //given
            memberRepository.stub(member);
            hubRepository.stub(hub);

            FindHubLinksCommand command = new FindHubLinksCommand(1L, hub.getHubId(), null, 0, 10);

            //when
            Exception exception = catchException(() -> linkService.findHubLinks(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Nested
        @DisplayName("비공개 허브인 경우")
        class WhenHubIsPrivate {

            @Test
            @DisplayName("성공: 사용자가 허브 소속인 경우 링크 목록 조회됨")
            void findHubLinks_WhenMemberIsHubMember() {
                //given
                Hub privateHub = HubFixture.privateHub(member);
                memberRepository.stub(member);
                hubRepository.stub(privateHub);
                hubMemberRepository.stub(privateHub, member);
                linkBundleRepository.stub(linkBundle);
                linkRepository.stub(link);

                FindHubLinksCommand command = new FindHubLinksCommand(linkBundle.getLinkBundleId(),
                    privateHub.getHubId(), member.getMemberId(), 0, 10);

                //when
                FindLinksResponse response = linkService.findHubLinks(command);

                //then
                assertThat(response.totalElements()).isEqualTo(1);
                assertThat(response.hasNext()).isFalse();
                assertThat(response.links()).hasSize(1)
                    .allSatisfy(findLink -> {
                        assertThat(findLink.linkId()).isEqualTo(link.getLinkId());
                        assertThat(findLink.url()).isEqualTo(link.getUrl());
                        assertThat(findLink.description()).isEqualTo(link.getDescription());
                    });
            }

            @Test
            @DisplayName("예외(Unauthenticated): 인증되지 않은 사용자")
            void unauthenticated_WhenMemberIsUnauthenticated() {
                //given
                Hub hub = HubFixture.privateHub(member);
                memberRepository.stub(member);
                hubRepository.stub(hub);
                linkBundleRepository.stub(linkBundle);
                linkRepository.stub(link);

                FindHubLinksCommand command = new FindHubLinksCommand(linkBundle.getLinkBundleId(),
                    hub.getHubId(), null, 0, 10);

                //when
                Exception exception = catchException(() -> linkService.findHubLinks(command));

                //then
                assertThat(exception).isInstanceOf(ShoutLinkException.class)
                    .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                    .isEqualTo(ErrorCode.UNAUTHENTICATED);
            }

            @Test
            @DisplayName("예외(Unauthorized): 사용자가 허브 소속이 아닐 때")
            void unauthorized_WhenMemberIsNotHubMember() {
                //given
                Hub privateHub = HubFixture.privateHub(member);
                memberRepository.stub(member);
                hubRepository.stub(privateHub);
                hubMemberRepository.stub(hub, member);
                linkBundleRepository.stub(linkBundle);
                linkRepository.stub(link);

                Member anotherMember = new Member(member.getMemberId() + 1, "email@email.com",
                    "asdf123!", "nickname", MemberRole.ROLE_USER);
                memberRepository.stub(anotherMember);
                FindHubLinksCommand command = new FindHubLinksCommand(linkBundle.getLinkBundleId(),
                    hub.getHubId(), anotherMember.getMemberId(), 0, 10);

                //when
                Exception exception = catchException(() -> linkService.findHubLinks(command));

                //then
                assertThat(exception).isInstanceOf(ShoutLinkException.class)
                    .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                    .isEqualTo(ErrorCode.UNAUTHORIZED);
            }
        }
    }

    @Nested
    @DisplayName("deleteLink 호출 시")
    class DeleteLinkTest {

        @Test
        @DisplayName("성공: 링크 삭제됨")
        void deleteLink() {
            //given
            Member member = MemberFixture.member();
            Link link = LinkFixture.link();
            DeleteLinkCommand command = new DeleteLinkCommand(member.getMemberId(),
                link.getLinkId());
            memberRepository.stub(member);
            linkRepository.stub(link);

            //when
            linkService.deleteLink(command);

            //then
            assertThat(linkRepository.count()).isZero();
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 링크")
        void notFound_WhenLinkNotFound() {
            //given
            Member member = MemberFixture.member();
            memberRepository.stub(member);
            DeleteLinkCommand command = new DeleteLinkCommand(member.getMemberId(), 1L);

            //when
            Exception exception = catchException(() -> linkService.deleteLink(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 회원")
        void notFound_WhenMemberNotFound() {
            //given
            DeleteLinkCommand command = new DeleteLinkCommand(1L, 1L);

            //when
            Exception exception = catchException(() -> linkService.deleteLink(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }
}
