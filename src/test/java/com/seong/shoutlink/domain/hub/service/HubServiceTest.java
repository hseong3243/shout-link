package com.seong.shoutlink.domain.hub.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.common.StubEventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.repository.StubHubRepository;
import com.seong.shoutlink.domain.hub.repository.StubHubTagReader;
import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.request.FindHubCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubDetailResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubsCommand;
import com.seong.shoutlink.domain.hub.service.response.FindHubsResponse;
import com.seong.shoutlink.domain.hub.service.result.HubTagResponse;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HubServiceTest {

    private HubService hubService;
    private StubMemberRepository memberRepository;
    private StubHubRepository hubRepository;
    private StubEventPublisher eventPublisher;
    private StubHubTagReader hubTagReader;

    @Nested
    @DisplayName("createHub 메서드 호출 시")
    class CreateHubTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            eventPublisher = new StubEventPublisher();
            hubTagReader = new StubHubTagReader();
            hubService = new HubService(memberRepository, hubRepository, hubTagReader, eventPublisher);
        }

        @Test
        @DisplayName("성공: 허브 생성됨")
        void createHub() {
            //given
            Member member = MemberFixture.member();
            memberRepository.stub(member);
            CreateHubCommand command = new CreateHubCommand(member.getMemberId(), "허브 이름", "허브 설명",
                false);

            //when
            CreateHubResponse response = hubService.createHub(command);

            //then
            assertThat(response.hubId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("성공: 허브 생성 이벤트 발행 됨")
        void createHub_ThenPublishCreateHubEvent() {
            //given
            Member member = MemberFixture.member();
            memberRepository.stub(member);
            CreateHubCommand command = new CreateHubCommand(member.getMemberId(), "허브 이름", "허브 설명",
                false);

            //when
            CreateHubResponse response = hubService.createHub(command);

            //then
            assertThat(eventPublisher.getPublishEventCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("예외(NotFount): 존재하지 않는 사용자")
        void notFound_WhenUserNotFound() {
            //given
            CreateHubCommand command = new CreateHubCommand(1L, "허브 이름", "허브 설명", false);

            //when
            Exception exception = catchException(() -> hubService.createHub(command));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findHubs 메서드 호출시")
    class FindHubsTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            eventPublisher = new StubEventPublisher();
            hubTagReader = new StubHubTagReader();
            hubService = new HubService(memberRepository, hubRepository, hubTagReader, eventPublisher);
        }

        @Test
        @DisplayName("성공: 허브 목록 조회됨")
        void findHubs() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            hubRepository.stub(hub);
            FindHubsCommand command = new FindHubsCommand(0, 10);

            //when
            FindHubsResponse response = hubService.findHubs(command);

            //then
            assertThat(response.hubs())
                .hasSize(1)
                .allSatisfy(findHub -> {
                    assertThat(findHub.hubId()).isEqualTo(hub.getHubId());
                    assertThat(findHub.masterId()).isEqualTo(hub.getMasterId());
                    assertThat(findHub.name()).isEqualTo(hub.getName());
                    assertThat(findHub.description()).isEqualTo(hub.getDescription());
                    assertThat(findHub.isPrivate()).isEqualTo(hub.isPrivate());
                });
        }

        @Test
        @DisplayName("성공: 태그와 함께 조회됨")
        void findHubsWithTags() {
            //given
            Member member = MemberFixture.member();
            Hub hubA = new Hub(1L, member.getMemberId(), "허브1", "설명", false);
            Hub hubB = new Hub(2L, member.getMemberId(), "허브2", "설명", false);
            HubTagResult tagA = new HubTagResult(1L, "태그A", hubA);
            HubTagResult tagB = new HubTagResult(2L, "태그B", hubA);
            HubTagResult tagC = new HubTagResult(3L, "태그C", hubB);
            HubTagResult tagD = new HubTagResult(4L, "태그D", hubB);
            hubRepository.stub(hubA, hubB);
            hubTagReader.stub(tagA, tagB, tagC, tagD);
            FindHubsCommand command = new FindHubsCommand(0, 10);

            //when
            FindHubsResponse response = hubService.findHubs(command);

            //then
            assertThat(response.hubs()).hasSize(2);
            assertThat(response.hubs()).filteredOn(hub -> hub.hubId().equals(hubA.getHubId()))
                .flatMap(hub -> hub.tags().stream().map(HubTagResponse::name).toList())
                .containsExactly(tagA.name(), tagB.name());
            assertThat(response.hubs()).filteredOn(hub -> hub.hubId().equals(hubB.getHubId()))
                .flatMap(hub -> hub.tags().stream().map(HubTagResponse::name).toList())
                .containsExactly(tagC.name(), tagD.name());
        }
    }

    @Nested
    @DisplayName("findHub 메서드 호출 시")
    class FindHubTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            hubRepository = new StubHubRepository();
            eventPublisher = new StubEventPublisher();
            hubTagReader = new StubHubTagReader();
            hubService = new HubService(memberRepository, hubRepository, hubTagReader, eventPublisher);
        }

        @Test
        @DisplayName("성공: 허브 조회됨")
        void findHub() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);
            memberRepository.stub(member);
            hubRepository.stub(hub, member);
            FindHubCommand command = new FindHubCommand(hub.getHubId());

            //when
            FindHubDetailResponse findHub = hubService.findHub(command);

            //then
            assertThat(findHub.hubId()).isEqualTo(hub.getMasterId());
            assertThat(findHub.name()).isEqualTo(hub.getName());
            assertThat(findHub.description()).isEqualTo(hub.getDescription());
            assertThat(findHub.isPrivate()).isEqualTo(hub.isPrivate());
            assertThat(findHub.masterId()).isEqualTo(hub.getMasterId());
            assertThat(findHub.masterNickname()).isEqualTo(member.getNickname());
        }

        @Test
        @DisplayName("예외(NotFound): 존재하지 않는 허브")
        void notFound_WhenHubNotFound() {
            //given
            FindHubCommand command = new FindHubCommand(1L);

            //when
            Exception exception = catchException(() -> hubService.findHub(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }
}
