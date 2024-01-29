package com.seong.shoutlink.domain.linkbundle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.linkbundle.repository.FakeLinkBundleRepository;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleCommand;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkBundleServiceTest {

    @Nested
    @DisplayName("createLinkBundle 메서드 호출 시")
    class CreateLinkBundleTest {

        private Member savedMember;
        private LinkBundleService linkBundleService;
        private StubMemberRepository memberRepository;
        private FakeLinkBundleRepository linkBundleRepository;

        @BeforeEach
        void setUp() {
            savedMember = MemberFixture.member();
            memberRepository = new StubMemberRepository(savedMember);
            linkBundleRepository = new FakeLinkBundleRepository();
            linkBundleService = new LinkBundleService(memberRepository, linkBundleRepository);
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
}
