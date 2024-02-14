package com.seong.shoutlink.domain.linkbundle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.repository.FakeLinkBundleRepository;
import com.seong.shoutlink.domain.linkbundle.service.request.CreateHubLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.request.FindLinkBundlesCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundlesResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import com.seong.shoutlink.fixture.StubHubRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkBundleServiceTest {

    private LinkBundleService linkBundleService;
    private StubMemberRepository memberRepository;
    private FakeLinkBundleRepository linkBundleRepository;
    private StubHubRepository hubRepository;

    @Nested
    @DisplayName("createLinkBundle 메서드 호출 시")
    class CreateLinkBundleTest {

        private Member savedMember;

        @BeforeEach
        void setUp() {
            savedMember = MemberFixture.member();
            memberRepository = new StubMemberRepository(savedMember);
            linkBundleRepository = new FakeLinkBundleRepository();
            hubRepository = new StubHubRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                linkBundleRepository);
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
            linkBundleRepository = new FakeLinkBundleRepository();
            hubRepository = new StubHubRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                linkBundleRepository);
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
            linkBundleRepository = new FakeLinkBundleRepository();
            linkBundleService = new LinkBundleService(memberRepository, hubRepository,
                linkBundleRepository);
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
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            hubRepository.stub(hub);
            long unknownMemberId = member.getMemberId() + 1;
            CreateHubLinkBundleCommand command = new CreateHubLinkBundleCommand(
                hub.getHubId(),
                unknownMemberId,
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
}
