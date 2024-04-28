package com.seong.shoutlink.domain.linkbundle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hubmember.repository.StubHubMemberRepository;
import com.seong.shoutlink.domain.link.service.LinkBundleService;
import com.seong.shoutlink.domain.link.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.repository.StubLinkBundleRepository;
import com.seong.shoutlink.domain.link.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.link.service.request.FindHubLinkBundlesCommand;
import com.seong.shoutlink.domain.link.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinkBundleResponse;
import com.seong.shoutlink.domain.link.service.response.FindLinkBundlesResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import com.seong.shoutlink.domain.hub.repository.StubHubRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkBundleServiceTest {

    private LinkBundleService linkBundleService;
    private StubMemberRepository memberRepository;
    private StubLinkBundleRepository linkBundleRepository;
    private StubHubRepository hubRepository;
    private StubHubMemberRepository hubMemberRepository;

    @Nested
    @DisplayName("createLinkBundle 메서드 호출 시")
    class CreateLinkBundleTest {

        private Member savedMember;

        @BeforeEach
        void setUp() {
            savedMember = MemberFixture.member();
            memberRepository = new StubMemberRepository(savedMember);
            linkBundleRepository = new StubLinkBundleRepository();
            hubRepository = new StubHubRepository();
            hubMemberRepository = new StubHubMemberRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                hubMemberRepository, linkBundleRepository);
        }

        @Test
        @DisplayName("성공: 링크 번들 생성됨")
        void createLinkBundle() {
            //given
            CreateLinkBundleCommand command = new CreateLinkBundleCommand(
                savedMember.getMemberId(),
                "간단한 설명",
                true);

            //when
            CreateLinkBundleResponse response = linkBundleService.createLinkBundle(command);

            //then
            assertThat(response.linkBundleId()).isEqualTo(1);
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 사용자")
        void notFound_WhenMemberNotFound() {
            //given
            Long notFoundMember = savedMember.getMemberId() + 1;
            CreateLinkBundleCommand command = new CreateLinkBundleCommand(
                notFoundMember,
                "간단한 설명",
                true);

            //when
            Exception exception = catchException(() -> linkBundleService.createLinkBundle(command));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findLinkBundles 메서드 호출 시")
    class FindLinkBundlesTest {

        private LinkBundleService linkBundleService;

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            linkBundleRepository = new StubLinkBundleRepository();
            hubRepository = new StubHubRepository();
            hubMemberRepository = new StubHubMemberRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                hubMemberRepository, linkBundleRepository);
        }

        @Test
        @DisplayName("성공: 링크 번들 목록 조회됨")
        void findLinkBundles() {
            //given
            Member stubMember = MemberFixture.member();
            memberRepository.stub(stubMember);
            linkBundleRepository.stub(
                new LinkBundle(1L, "기본", true),
                new LinkBundle(2L, "두번째", false)
            );
            FindLinkBundlesCommand command = new FindLinkBundlesCommand(stubMember.getMemberId());

            //when
            FindLinkBundlesResponse response = linkBundleService.findLinkBundles(command);

            //then
            List<FindLinkBundleResponse> linkBundles = response.linkBundles();
            assertThat(linkBundles)
                .hasSize(2);
            assertThat(linkBundles.get(0))
                .isEqualTo(new FindLinkBundleResponse(1L, "기본", true));
            assertThat(linkBundles.get(1))
                .isEqualTo(new FindLinkBundleResponse(2L, "두번째", false));
        }
    }

    @Nested
    @DisplayName("createHubLinkBundle 메서드 호출 시")
    class CreateHubLinkBundleTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            linkBundleRepository = new StubLinkBundleRepository();
            hubMemberRepository = new StubHubMemberRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                hubMemberRepository, linkBundleRepository);
        }

        @Test
        @DisplayName("성공: 허브 링크 번들 생성됨")
        void createHubLinkBundle() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            hubRepository.stub(hub);
            CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(
                hub.getHubId(),
                member.getMemberId(),
                "테스트 허브",
                false);

            //when
            CreateLinkBundleResponse response = linkBundleService.createHubLinkBundle(command);

            //then
            assertThat(response.linkBundleId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 허브")
        void notFound_WhenHubNotFound() {
            //given
            CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(1L, 1L, "허브",
                false);

            //when
            Exception exception = catchException(
                () -> linkBundleService.createHubLinkBundle(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(Unauthorized): 링크 묶음 생성 권한 없음")
        void unauthorized_WhenMemberIsNotHubMaster() {
            //given
            Member master = MemberFixture.member();
            Hub hub = HubFixture.hub(master);
            Member member = new Member(2L, "asdf1234@gmail.com", "asdf1234!", "asdf",
                MemberRole.ROLE_USER);
            memberRepository.stub(master);
            memberRepository.stub(member);
            hubRepository.stub(hub);
            CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(
                hub.getHubId(),
                member.getMemberId(),
                "허브",
                false);

            //when
            Exception exception = catchException(
                () -> linkBundleService.createHubLinkBundle(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("findHubLinkBundles 메서드 호출 시")
    class FindHubLinkBundlesTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            linkBundleRepository = new StubLinkBundleRepository();
            hubMemberRepository = new StubHubMemberRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                hubMemberRepository, linkBundleRepository);
        }

        @Test
        @DisplayName("성공: 허브 링크 묶음 목록 조회됨")
        void findHubLinkBundles() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            memberRepository.stub(member);
            hubRepository.stub(hub);
            linkBundleRepository.stub(linkBundle);
            FindHubLinkBundlesCommand command = new FindHubLinkBundlesCommand(1L,
                member.getMemberId());

            //when
            FindLinkBundlesResponse response = linkBundleService.findHubLinkBundles(command);

            //then
            assertThat(response.linkBundles()).hasSize(1)
                .allSatisfy(findLinkBundle -> {
                    assertThat(findLinkBundle.linkBundleId())
                        .isEqualTo(linkBundle.getLinkBundleId());
                    assertThat(findLinkBundle.description()).isEqualTo(linkBundle.getDescription());
                    assertThat(findLinkBundle.isDefault()).isEqualTo(linkBundle.isDefault());
                });
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 허브")
        void notFound_WhenHubNotFound() {
            //given
            FindHubLinkBundlesCommand command = new FindHubLinkBundlesCommand(1L, 1L);

            //when
            Exception exception = catchException(
                () -> linkBundleService.findHubLinkBundles(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Nested
        @DisplayName("비공개 허브인 경우")
        class WhenHubIsPrivate {

            @Test
            @DisplayName("성공: 사용자가 허브 소속일 때 허브 링크 목록 조회됨")
            void findHubLinkBundles_WhenMemberIsHubMember() {
                //given
                Member member = MemberFixture.member();
                Hub hub = HubFixture.privateHub(member);
                LinkBundle linkBundle = LinkBundleFixture.linkBundle();
                memberRepository.stub(member);
                hubRepository.stub(hub);
                linkBundleRepository.stub(linkBundle);
                hubMemberRepository.stub(hub, member);
                FindHubLinkBundlesCommand command = new FindHubLinkBundlesCommand(1L,
                    member.getMemberId());

                //when
                FindLinkBundlesResponse response = linkBundleService.findHubLinkBundles(command);

                //then
                assertThat(response.linkBundles()).hasSize(1)
                    .allSatisfy(findLinkBundle -> {
                        assertThat(findLinkBundle.linkBundleId()).isEqualTo(
                            linkBundle.getLinkBundleId());
                        assertThat(findLinkBundle.description()).isEqualTo(
                            linkBundle.getDescription());
                        assertThat(findLinkBundle.isDefault()).isEqualTo(linkBundle.isDefault());
                    });
            }

            @Test
            @DisplayName("예외(Unauthenticated): 인증되지 않은 사용자")
            void unauthenticated_WhenMemberIsUnauthenticated() {
                //given
                Member member = MemberFixture.member();
                Hub hub = HubFixture.privateHub(member);
                memberRepository.stub(member);
                hubRepository.stub(hub);
                FindHubLinkBundlesCommand command = new FindHubLinkBundlesCommand(hub.getHubId(),
                    null);

                //when
                Exception exception = catchException(
                    () -> linkBundleService.findHubLinkBundles(command));

                //then
                assertThat(exception).isInstanceOf(ShoutLinkException.class)
                    .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                    .isEqualTo(ErrorCode.UNAUTHENTICATED);
            }

            @Test
            @DisplayName("예외(Unauthorized): 사용자가 허브 소속이 아닐 때")
            void unauthorized_WhenMemberIsNotHubMember() {
                //given
                Member member = MemberFixture.member();
                Hub hub = HubFixture.privateHub(member);
                memberRepository.stub(member);
                hubRepository.stub(hub);
                hubMemberRepository.stub(hub, member);

                Member anotherMember = new Member(member.getMemberId() + 1, "email@email.com",
                    "asdf123!", "nickname", MemberRole.ROLE_USER);
                memberRepository.stub(anotherMember);
                FindHubLinkBundlesCommand command = new FindHubLinkBundlesCommand(hub.getHubId(),
                    anotherMember.getMemberId());

                //when
                Exception exception = catchException(
                    () -> linkBundleService.findHubLinkBundles(command));

                //then
                assertThat(exception).isInstanceOf(ShoutLinkException.class)
                    .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                    .isEqualTo(ErrorCode.UNAUTHORIZED);
            }
        }
    }
}
